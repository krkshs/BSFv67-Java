package Core.Byte;

public class wtr {
    private final bytestrm s;

    public wtr(bytestrm stream) {
        s = stream;
    }

    public void i32(int v) { s.write_int(v); }
    public void i16(short v) { s.write_short(v); }
    public void i8(byte v) { s.write_byte(v); }
    public void vint(int v) { s.write_vint(v); }
    public void str(String v) { s.write_string(v); }
    public void str_vint(String v) { s.write_string_vint(v); }
    public void long_val(int a, int b) { s.write_long(a, b); }
    public void logic_long(int a, int b) { s.write_logic_long(a, b); }
    public void bool(boolean v) { s.write_boolean(v); }
    public void hex(String v) { s.write_hex(v); }
    public void bytes(byte[] v) { s.write_bytes(v); }
    public void data_ref(int a, int b) { s.write_data_ref(a, b); }
}
