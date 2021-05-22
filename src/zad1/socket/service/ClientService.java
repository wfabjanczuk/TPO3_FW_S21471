package zad1.socket.service;

import zad1.constant.Message;
import zad1.serialization.Json;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ClientService extends TopicService {
    public List<String> subscribedTopics = new LinkedList<>();

    public ClientService(String host, int port) {
        super(host, port);
    }

    public String subscribeToTopic(String topic) throws IOException {
        String addTopicMessage = Message.addTopic + " " + Json.serializeString(topic);

        printWriter.println(addTopicMessage);
        logSent(addTopicMessage);

        String result = bufferedReader.readLine();
        logReceived(result);

        return result;
    }

    public String unsubscribeFromTopic(String topic) throws IOException {
        String deleteTopicMessage = Message.deleteTopic + " " + Json.serializeString(topic);

        printWriter.println(deleteTopicMessage);
        logSent(deleteTopicMessage);

        String result = bufferedReader.readLine();
        logReceived(result);

        return result;
    }

    @Override
    protected void sayGoodbye() {
        try {
            printWriter.println(Message.goodbyeFromClient);
            logSent(Message.goodbyeFromClient);
        } catch (Exception exception) {
            logException(exception);
        }
    }
}
