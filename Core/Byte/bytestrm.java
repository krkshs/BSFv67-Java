package Core.Byte;

import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

public class bytestrm {
    public ArrayList<Byte> buf;
    public int offset;
    public int bit_offset;

    public bytestrm() {
        buf = new ArrayList<>();
        offset = 0;
        bit_offset = 0;
    }

    public bytestrm(byte[] data) {
        buf = new ArrayList<>(data.length);
        for (byte b : data) {
            buf.add(b);
        }
        offset = 0;
        bit_offset = 0;
    }

    public byte[] toArray() {
        byte[] arr = new byte[buf.size()];
        for (int i = 0; i < buf.size(); i++) {
            arr[i] = buf.get(i);
        }
        return arr;
    }

    public int read_int() {
        bit_offset = 0;
        int val = ((buf.get(offset) & 0xFF) << 24) |
                  ((buf.get(offset + 1) & 0xFF) << 16) |
                  ((buf.get(offset + 2) & 0xFF) << 8) |
                  (buf.get(offset + 3) & 0xFF);
        offset += 4;
        return val;
    }

    public short read_short() {
        bit_offset = 0;
        short val = (short)(((buf.get(offset) & 0xFF) << 8) | (buf.get(offset + 1) & 0xFF));
        offset += 2;
        return val;
    }

    public String read_string() {
        int length = read_int();
        if (length <= 0 || length >= 90000) {
            return "";
        }
        byte[] temp = new byte[length];
        for (int i = 0; i < length; i++) {
            temp[i] = buf.get(offset + i);
        }
        String str = new String(temp, StandardCharsets.UTF_8);
        offset += length;
        return str;
    }

    public int read_vint() {
        bit_offset = 0;
        int result = 0;
        int shift = 0;

        int b = buf.get(offset) & 0xFF;
        offset += 1;

        int a1 = (b & 0x40) >> 6;
        int a2 = (b & 0x80) >> 7;
        int s = (b << 1) & 0x7E;
        b = s | (a2 << 7) | a1;

        result |= (b & 0x7F) << shift;
        shift += 7;

        while ((b & 0x80) != 0) {
            if (shift > 28) break;
            b = buf.get(offset) & 0xFF;
            offset += 1;
            result |= (b & 0x7F) << shift;
            shift += 7;
        }

        int r = result;
        return (r >> 1) ^ (-(r & 1));
    }

    public boolean read_boolean() {
        return read_vint() >= 1;
    }

    public int[] read_logic_long() {
        return new int[]{read_vint(), read_vint()};
    }

    public int[] read_long() {
        return new int[]{read_int(), read_int()};
    }

    public int[] read_data_ref() {
        int a = read_vint();
        return new int[]{a, a == 0 ? 0 : read_vint()};
    }

    public void write_byte(byte value) {
        bit_offset = 0;
        buf.add(value);
        offset += 1;
    }

    public void write_short(short value) {
        bit_offset = 0;
        buf.add((byte)((value >> 8) & 0xFF));
        buf.add((byte)(value & 0xFF));
        offset += 2;
    }

    public void write_int(int value) {
        bit_offset = 0;
        buf.add((byte)((value >> 24) & 0xFF));
        buf.add((byte)((value >> 16) & 0xFF));
        buf.add((byte)((value >> 8) & 0xFF));
        buf.add((byte)(value & 0xFF));
        offset += 4;
    }

    public void write_vint(int value) {
        bit_offset = 0;
        int v = value;

        int flippedSigned = value ^ (value >> 31);
        int flipped = flippedSigned;

        int temp = (v >> 25) & 0x40;
        temp |= v & 0x3F;
        v >>= 6;
        flipped >>= 6;

        if (flipped == 0) {
            write_byte((byte)temp);
            return;
        }

        write_byte((byte)(temp | 0x80));
        flipped >>= 7;

        int r = (flipped != 0) ? 0x80 : 0;
        write_byte((byte)((v & 0x7F) | r));
        v >>= 7;

        while (flipped != 0) {
            flipped >>= 7;
            r = (flipped != 0) ? 0x80 : 0;
            write_byte((byte)((v & 0x7F) | r));
            v >>= 7;
        }
    }

    public void write_boolean(boolean value) {
        if (bit_offset == 0) {
            buf.add((byte)0);
            offset += 1;
        }
        if (value) {
            byte last = buf.get(offset - 1);
            last |= (byte)(1 << bit_offset);
            buf.set(offset - 1, last);
        }
        bit_offset = (bit_offset + 1) & 7;
    }

    public void write_string(String value) {
        if (value != null) {
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            if (bytes.length > 90000) {
                write_int(-1);
                return;
            }
            write_int(bytes.length);
            for (byte b : bytes) {
                buf.add(b);
            }
            offset += bytes.length;
        } else {
            write_int(-1);
        }
    }

    public void write_string_vint(String value) {
        if (value != null) {
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            write_vint(bytes.length);
            for (byte b : bytes) {
                buf.add(b);
            }
            offset += bytes.length;
        } else {
            write_vint(0);
        }
    }

    public void write_long(int v1, int v2) {
        write_int(v1);
        write_int(v2);
    }

    public void write_long_long(long value) {
        write_int((int)(value >> 32));
        write_int((int)value);
    }

    public void write_logic_long(int v1, int v2) {
        write_vint(v1);
        write_vint(v2);
    }

    public void write_data_ref(int v1, int v2) {
        if (v1 < 1) {
            write_vint(0);
        } else {
            write_vint(v1);
            write_vint(v2);
        }
    }

    public void write_bytes(byte[] data) {
        if (data != null) {
            write_int(data.length);
            for (byte b : data) {
                buf.add(b);
            }
            offset += data.length;
        } else {
            write_int(-1);
        }
    }

    public void write_hex(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("OddHexLength");
        }
        for (int i = 0; i < hex.length(); i += 2) {
            int val = Integer.parseInt(hex.substring(i, i + 2), 16);
            buf.add((byte)val);
            offset += 1;
        }
    }
}
