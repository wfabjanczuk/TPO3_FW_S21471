package zad1.socket.service;

import zad1.constant.Message;
import zad1.serialization.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminService extends TopicService {
    public AdminService(String host, int port) {
        super(host, port);
    }

    public List<String> getTopics() {
        printWriter.println(Message.getTopicsForAdmin);
        logSent(Message.getTopicsForAdmin);

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

    @Override
    protected void sayGoodbye() {
        try {
            printWriter.println(Message.goodbyeFromAdmin);
            logSent(Message.goodbyeFromAdmin);
        } catch (Exception exception) {
            logException(exception);
        }
    }
}
