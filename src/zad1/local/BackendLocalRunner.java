package zad1.local;

import zad1.service.Server;

public class BackendLocalRunner {
    public static void main(String[] args) {
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            Server server = new Server(host, port);
            server.start();
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
