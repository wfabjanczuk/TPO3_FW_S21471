package zad1;

public class AdminLocalRunner {
    public static void main(String[] args) {
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            Admin admin = new Admin(host, port);
            admin.start();
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
