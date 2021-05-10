package zad1;

import zad1.constant.Message;

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
            String getTopicsMessage = Message.getTopics;

            out.println(getTopicsMessage);
            logThreadSent(getTopicsMessage);

            String resp = in.readLine();
            logThreadReceived(resp);

            String goodbyeMessage = Message.goodbye;
            out.println(goodbyeMessage);
            logThreadSent(goodbyeMessage);

            sock.close();
        } catch (Exception exception) {
            logThreadException(exception);
        }
    }
}
