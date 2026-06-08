package Core;

import java.io.OutputStream;
import java.net.Socket;
import Core.Byte.bytestrm;

public class piranha {
    public bytestrm stream;
    public int id;
    public int version;
    public Socket conn;

    public piranha(int id, int version, Socket conn) {
        this.stream = new bytestrm();
        this.id = id;
        this.version = version;
        this.conn = conn;
    }

    public void send() throws Exception {
        if (id < 20000) return;

        byte[] body = stream.toArray();
        int bodyLen = body.length;

        byte[] header = new byte[7];
        header[0] = (byte)((id >> 8) & 0xFF);
        header[1] = (byte)(id & 0xFF);
        header[2] = (byte)((bodyLen >> 16) & 0xFF);
        header[3] = (byte)((bodyLen >> 8) & 0xFF);
        header[4] = (byte)(bodyLen & 0xFF);
        header[5] = (byte)((version >> 8) & 0xFF);
        header[6] = (byte)(version & 0xFF);

        OutputStream out = conn.getOutputStream();
        out.write(header);
        if (bodyLen > 0) {
            out.write(body);
        }
        out.flush();

        logger.packet_out(id, logger.packet_name(id), bodyLen);
    }
}
