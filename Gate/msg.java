package Gate;

import java.net.Socket;
import Core.logger;

public class msg {
    public static void dispatch(int id, byte[] payload, Socket client) throws Exception {
        switch (id) {
            case 10100:
                {
                    Message.Receive.Login.hello m = new Message.Receive.Login.hello(payload, client);
                    m.decode();
                    m.process();
                    break;
                }
            case 10101:
                {
                    Message.Receive.Login.auth m = new Message.Receive.Login.auth(payload, client);
                    m.decode();
                    m.process();
                    break;
                }
            default:
                logger.unknown(id);
                break;
        }
    }
}
