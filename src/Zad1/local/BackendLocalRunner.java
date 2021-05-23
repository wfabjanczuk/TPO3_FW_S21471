package Zad1.local;

import Zad1.socket.server.MainServer;

public class BackendLocalRunner {
    public static void main(String[] args) {
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            MainServer server = new MainServer(host, port);
            server.start();
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
