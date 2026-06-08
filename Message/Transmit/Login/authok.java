package Message.Transmit.Login;

import java.net.Socket;
import Core.piranha;
import Core.Byte.wtr;

public class authok {
    public piranha msg;

    public authok(Socket conn) {
        msg = new piranha(20104, 1, conn);
    }

    public void encode() {
        wtr w = new wtr(msg.stream);
        long ts = System.currentTimeMillis();
        String tsStr = String.valueOf(ts);
        w.long_val(0, 1);
        w.long_val(0, 1);
        w.str("psinatoken");
        w.str(null);
        w.str(null);
        w.i32(67);
        w.i32(264);
        w.i32(1);
        w.str("prod");
        w.i32(0);
        w.i32(0);
        w.i32(1);
        w.str("");
        w.str(tsStr);
        w.str(tsStr);
    }

    public void send() throws Exception {
        encode();
        msg.send();
    }
}
