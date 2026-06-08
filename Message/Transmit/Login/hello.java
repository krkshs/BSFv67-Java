package Message.Transmit.Login;

import java.net.Socket;
import Core.piranha;
import Core.Byte.wtr;

public class hello {
    public piranha msg;

    public hello(Socket conn) {
        msg = new piranha(20100, 0, conn);
    }

    public void encode() {
        wtr w = new wtr(msg.stream);
        w.i32(24);
        for (int i = 0; i < 24; i++) {
            w.i8((byte)1);
        }
    }

    public void send() throws Exception {
        encode();
        msg.send();
    }
}
