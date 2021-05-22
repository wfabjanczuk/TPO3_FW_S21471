package zad1.socket.service;

import zad1.socket.Loggable;
import zad1.constant.Message;
import zad1.serialization.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AdminService implements Loggable {
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public AdminService(String host, int port) {
        try {
            socket = new Socket(host, port);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception exception) {
            logException(exception);
            System.exit(1);
        }
    }

    public List<String> getTopics() {
        printWriter.println(Message.getTopics);
        logSent(Message.getTopics);

        try {
            String response = bufferedReader.readLine();

            logReceived(response);
            return Json.unserializeStrings(response);
        } catch (Exception exception) {
            logException(exception);
            return new ArrayList<>();
        }
    }

    public String addTopic(String topic) throws IOException {
        String addTopicMessage = Message.addTopic + " " + Json.serializeString(topic);

        printWriter.println(addTopicMessage);
        logSent(addTopicMessage);

        String result = bufferedReader.readLine();
        logReceived(result);

        return result;
    }

    public String deleteTopic(String topic) throws IOException {
        String deleteTopicMessage = Message.deleteTopic + " " + Json.serializeString(topic);

        printWriter.println(deleteTopicMessage);
        logSent(deleteTopicMessage);

        String result = bufferedReader.readLine();
        logReceived(result);

        return result;
    }

    public String publishMessage(String message, String topic) throws IOException {
        String publishMessage = Message.publish + " " + Json.serializeStrings(message, topic);

        printWriter.println(publishMessage);
        logSent(publishMessage);

        String result = bufferedReader.readLine();
        logReceived(result);

        return result;
    }

    public void closeResources() {
        try {
            if (socket != null) {
                printWriter.println(Message.goodbyeFromAdmin);
                logSent(Message.goodbyeFromAdmin);
                socket.close();
            }

            logConnectionResourcesClosed();
            logConnectionClosed();
        } catch (IOException exception) {
            logException(exception);
        }
    }
}
