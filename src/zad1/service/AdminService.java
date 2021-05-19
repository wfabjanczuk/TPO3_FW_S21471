package zad1.service;

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

//    public void run() {
//        try {
//            printWriter.println(Message.getTopics);
//            logSent(Message.getTopics);
//            setLocalTopics(bufferedReader.readLine());
//            showTopics();
//
//            String addTopicMessage = Message.addTopic + " " + Json.serializeString("technology");
//
//            printWriter.println(addTopicMessage);
//            logSent(addTopicMessage);
//            logReceived(bufferedReader.readLine());
//
//            printWriter.println(Message.getTopics);
//            logSent(Message.getTopics);
//            setLocalTopics(bufferedReader.readLine());
//            showTopics();
//
//            String removeTopicMessage = Message.removeTopic + " " + Json.serializeString("food");
//

//
//            printWriter.println(Message.getTopics);
//            logSent(Message.getTopics);
//            setLocalTopics(bufferedReader.readLine());
//            showTopics();
//
//            printWriter.println(Message.goodbye);
//            logSent(Message.goodbye);
//
//            socket.close();
//        } catch (Exception exception) {
//            logException(exception);
//        }
//    }

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
}
