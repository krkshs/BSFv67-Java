//srv
// ___ ___ _ _ 
//|_ -|  _| | |
//|___|_|  \_/ 
import java.io.IOException;
import java.io.InputStream;
import java.io.EOFException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import Core.logger;
import Core.cfg;
import Gate.msg;

public class server {
    public static void main(String[] args) {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(cfg.port, 50, InetAddress.getByName(cfg.ip));
            listener.setReuseAddress(true);
        } catch (Exception ex) {
            logger.server_info("Failed to start listener: " + ex.getMessage());
            return;
        }

        logger.banner();
        logger.server_info("Listening on " + cfg.port);

        while (true) {
            try {
                Socket client = listener.accept();
                new Thread(() -> client_loop(client)).start();
            } catch (Exception ex) {
                logger.server_info("Accept failed: " + ex.getMessage());
            }
        }
    }

    private static void client_loop(Socket client) {
        try {
            String ip = "client";
            try {
                InetAddress addr = client.getInetAddress();
                if (addr != null) {
                    ip = addr.getHostAddress();
                }
            } catch (Exception e) {}

            logger.set_addr_str(ip);
            logger.connect();

            InputStream in = client.getInputStream();

            while (true) {
                byte[] header;
                try {
                    header = readExactly(in, 7);
                } catch (EOFException ex) {
                    logger.disconnect();
                    break;
                } catch (Exception ex) {
                    logger.client_err("Read failed  " + ex.getMessage());
                    break;
                }

                int msgId = ((header[0] & 0xFF) << 8) | (header[1] & 0xFF);
                int msgLen = ((header[2] & 0xFF) << 16) | ((header[3] & 0xFF) << 8) | (header[4] & 0xFF);
                int msgVer = ((header[5] & 0xFF) << 8) | (header[6] & 0xFF);

                byte[] payload = new byte[msgLen];
                if (msgLen > 0) {
                    try {
                        payload = readExactly(in, msgLen);
                    } catch (EOFException ex) {
                        logger.disconnect();
                        break;
                    } catch (Exception ex) {
                        logger.client_err("Payload read failed  " + ex.getMessage());
                        break;
                    }
                }

                logger.packet_in(msgId, logger.packet_name(msgId), msgLen);

                try {
                    msg.dispatch(msgId, payload, client);
                } catch (Exception ex) {
                    logger.client_err("Dispatch failed on " + msgId + "  " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            logger.client_err("Client exception: " + ex.getMessage());
        } finally {
            try {
                client.close();
            } catch (Exception e) {}
        }
    }

    private static byte[] readExactly(InputStream in, int count) throws IOException {
        byte[] buffer = new byte[count];
        int read = 0;
        while (read < count) {
            int r = in.read(buffer, read, count - read);
            if (r < 0) {
                throw new EOFException();
            }
            read += r;
        }
        return buffer;
    }
}
