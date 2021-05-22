package zad1.local;

import zad1.gui.ClientGui;
import zad1.socket.server.MessageInboxServer;
import zad1.socket.service.ClientService;

public class ClientLocalRunner {
    public static void main(String[] args) {
        try {
            String mainServerHost = args[0];
            int mainServerPort = Integer.parseInt(args[1]);

            String messageInboxHost = args[2];
            int messageInboxPort = Integer.parseInt(args[3]);

            ClientGui clientGui = new ClientGui();
            clientGui.initialize(
                    new ClientService(mainServerHost, mainServerPort),
                    new MessageInboxServer(messageInboxHost, messageInboxPort)
            );
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
