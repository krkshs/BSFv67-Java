package Core;

public class logger {
    private static final ThreadLocal<String> addr = ThreadLocal.withInitial(() -> "client");

    private static int term_width() {
        return 80;
    }

    private static void center(int visible) {
        int w = term_width();
        if (w > visible) {
            int spaces = (w - visible) / 2;
            for (int i = 0; i < spaces; i++) {
                System.out.print(" ");
            }
        }
    }

    public static void set_addr_str(String ip) {
        addr.set(ip);
    }

    public static String get_addr() {
        return addr.get();
    }

    public static void banner() {
        String[] lines = new String[] {
            "        ____ _____ ______      __________",
            "       / __ ) ___// ____/   __/ ___/__  /",
            "      / __  \\__ \\/ /_  | | / / __ \\  / / ",
            "     / /_/ /__/ / __/  | |/ / /_/ / / /  ",
            "    /_____/____/_/     |___/\\____/ /_/   ",
            "    Brawl Stars V67 | github.com/FMZNkdv"
        };
        String[] colors = new String[] {
            "\u001B[38;2;0;255;255m",
            "\u001B[38;2;0;180;255m",
            "\u001B[38;2;80;100;255m",
            "\u001B[38;2;160;60;255m",
            "\u001B[38;2;220;40;220m",
            "\u001B[38;2;255;180;220m"
        };
        int lineLen = 46;
        System.out.println();
        for (int i = 0; i < lines.length; i++) {
            center(lineLen);
            System.out.println(colors[i] + "\u001B[1m" + lines[i] + "\u001B[0m");
        }
    }

    public static void server_info(String msg) {
        center(10 + 2 + msg.length());
        System.out.println("\u001B[36m\u001B[1m[ SERVER ]\u001B[0m  " + msg);
    }

    public static void connect() {
        String a = get_addr();
        center(2 + a.length() + 4 + 13);
        System.out.println("\u001B[32m\u001B[1m[ " + a + " ]\u001B[0m\u001B[32m  ●  Connected\u001B[0m");
    }

    public static void disconnect() {
        String a = get_addr();
        center(2 + a.length() + 4 + 16);
        System.out.println("\u001B[33m\u001B[1m[ " + a + " ]\u001B[0m\u001B[33m  ○  Disconnected\u001B[0m");
    }

    public static void packet_in(int id, String name, int len) {
        String a = get_addr();
        if ("Unknown".equals(name)) {
            String msg = "Not Found (" + id + ")";
            center(2 + a.length() + 4 + 5 + msg.length());
            System.out.println("\u001B[34m\u001B[1m[ " + a + " ]\u001B[0m\u001B[34m  ←  \u001B[0m\u001B[33m" + msg + "\u001B[0m");
        } else {
            center(2 + a.length() + 4 + 5 + name.length());
            System.out.println("\u001B[34m\u001B[1m[ " + a + " ]\u001B[0m\u001B[34m  ←  \u001B[0m" + name);
        }
    }

    public static void packet_out(int id, String name, int len) {
        String a = get_addr();
        if ("Unknown".equals(name)) {
            String msg = "Not Found (" + id + ")";
            center(2 + a.length() + 4 + 5 + msg.length());
            System.out.println("\u001B[35m\u001B[1m[ " + a + " ]\u001B[0m\u001B[35m  →  \u001B[0m\u001B[33m" + msg + "\u001B[0m");
        } else {
            center(2 + a.length() + 4 + 5 + name.length());
            System.out.println("\u001B[35m\u001B[1m[ " + a + " ]\u001B[0m\u001B[35m  →  \u001B[0m" + name);
        }
    }

    public static void unknown(int id) {
        String a = get_addr();
        String msg = "Not Found (" + id + ")";
        center(2 + a.length() + 4 + 5 + msg.length());
        System.out.println("\u001B[33m\u001B[1m[ " + a + " ]\u001B[0m\u001B[33m  ?  " + msg + "\u001B[0m");
    }

    public static void client_err(String msg) {
        String a = get_addr();
        center(2 + a.length() + 4 + 5 + msg.length());
        System.out.println("\u001B[31m\u001B[1m[ " + a + " ]  ✗  " + msg + "\u001B[0m");
    }

    public static String packet_name(int id) {
        switch (id) {
            case 10100: return "Hello";
            case 10101: return "Auth";
            case 20100: return "Hello";
            case 20104: return "AuthOk";
            case 24101: return "HomeData";
            default: return "Unknown";
        }
    }
}
