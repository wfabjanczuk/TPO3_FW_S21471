package zad1.service;

import zad1.constant.Message;
import zad1.serialization.Json;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Admin extends Thread implements LoggableThread {
    private List<String> topics;

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
            setLocalTopics(in.readLine());
            showTopics();

            String addTopicMessage = Message.addTopic + " " + Json.serializeString("technology");

            out.println(addTopicMessage);
            logThreadSent(addTopicMessage);
            logThreadReceived(in.readLine());

            out.println(Message.getTopics);
            logThreadSent(Message.getTopics);
            setLocalTopics(in.readLine());
            showTopics();

            String removeTopicMessage = Message.removeTopic + " " + Json.serializeString("food");

            out.println(removeTopicMessage);
            logThreadSent(removeTopicMessage);
            logThreadReceived(in.readLine());

            out.println(Message.getTopics);
            logThreadSent(Message.getTopics);
            setLocalTopics(in.readLine());
            showTopics();

            out.println(Message.goodbye);
            logThreadSent(Message.goodbye);

            sock.close();
        } catch (Exception exception) {
            logThreadException(exception);
        }
    }

    private void setLocalTopics(String response) {
        logThreadReceived(response);
        topics = Json.unserializeStrings(response);
    }

    private void showTopics() {
        System.out.println("\nLocal topics:");
        for (String topic : topics) {
            System.out.println("- " + topic);
        }
        System.out.println();
    }
}
