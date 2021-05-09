package zad1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Admin extends Thread implements LoggableThread {
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    public Admin(String host, int port) {
        try {
            sock = new Socket(host, port);
            out = new PrintWriter(sock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (Exception exception) {
            logThreadException(exception);
            System.exit(1);
        }
    }

    public void run() {
        try {
            String getTopicsMessage = "get topics";

            out.println(getTopicsMessage);
            logThreadSent(getTopicsMessage);

            String byeMessage = "bye";
            out.println(byeMessage);
            logThreadSent(byeMessage);

            sock.close();
        } catch (Exception exception) {
            logThreadException(exception);
        }
    }
}
