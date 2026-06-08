package Message.Receive.Login;

import java.net.Socket;
import Core.Byte.bytestrm;

public class auth {
    public bytestrm stream;
    public Socket conn;
    public int highId;
    public int lowId;
    public String token = "";
    public int major;
    public int build;
    public int content;

    public auth(byte[] payload, Socket client) {
        stream = new bytestrm(payload);
        conn = client;
    }

    public void decode() {
        highId = stream.read_int();
        lowId = stream.read_int();
        token = stream.read_string();
        major = stream.read_vint();
        build = stream.read_vint();
        content = stream.read_vint();
    }

    public void process() throws Exception {
        Thread.sleep(1000);

        Message.Transmit.Login.authok ok = new Message.Transmit.Login.authok(conn);
        ok.send();

        Message.Transmit.Home.owndata home = new Message.Transmit.Home.owndata(conn);
        home.send();
    }
}
