package Message.Receive.Login;

import java.net.Socket;
import Core.Byte.bytestrm;

public class hello {
    public bytestrm stream;
    public Socket conn;

    public hello(byte[] payload, Socket client) {
        stream = new bytestrm(payload);
        conn = client;
    }

    public void decode() {
    }

    public void process() throws Exception {
        Message.Transmit.Login.hello reply = new Message.Transmit.Login.hello(conn);
        reply.send();
    }
}
