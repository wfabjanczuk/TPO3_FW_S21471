package zad1;

import zad1.constant.Message;
import zad1.serialization.Json;

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
            out.println(Message.getTopics);
            logThreadSent(Message.getTopics);
            logThreadReceived(in.readLine());

            String setTopicsMessage = Message.setTopics + " " + Json.serializeArrayOfStrings(new String[]{"Topic_01", "Topic_02"});

            out.println(setTopicsMessage);
            logThreadSent(setTopicsMessage);
            logThreadReceived(in.readLine());

            out.println(Message.getTopics);
            logThreadSent(Message.getTopics);
            logThreadReceived(in.readLine());

            out.println(Message.goodbye);
            logThreadSent(Message.goodbye);

            sock.close();
        } catch (Exception exception) {
            logThreadException(exception);
        }
    }
}
