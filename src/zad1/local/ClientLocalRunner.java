package zad1.local;

import zad1.gui.ClientGui;
import zad1.socket.server.MessageInboxServer;

public class ClientLocalRunner {
    public static void main(String[] args) {
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            ClientGui clientGui = new ClientGui();
            clientGui.initialize(new MessageInboxServer(host, port));
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
