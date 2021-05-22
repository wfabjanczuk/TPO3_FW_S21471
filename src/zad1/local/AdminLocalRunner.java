package zad1.local;

import zad1.gui.AdminGui;
import zad1.socket.service.AdminService;

public class AdminLocalRunner {
    public static void main(String[] args) {
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            AdminGui adminGui = new AdminGui();
            adminGui.initialize(new AdminService(host, port));
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
