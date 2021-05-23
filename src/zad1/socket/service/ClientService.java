package zad1.socket.service;

import javafx.scene.control.ChoiceBox;
import zad1.constant.Message;
import zad1.serialization.Json;

import java.io.IOException;
import java.util.List;

public class ClientService extends TopicService {
    private ChoiceBox<String> topicToSubscribeChoiceBox;
    private ChoiceBox<String> topicToUnsubscribeChoiceBox;

    public ClientService(String host, int port) {
        super(host, port);
    }

    public void setTopicToSubscribeChoiceBox(ChoiceBox<String> topicToSubscribeChoiceBox) {
        this.topicToSubscribeChoiceBox = topicToSubscribeChoiceBox;
    }

    public void setTopicToUnsubscribeChoiceBox(ChoiceBox<String> topicToUnsubscribeChoiceBox) {
        this.topicToUnsubscribeChoiceBox = topicToUnsubscribeChoiceBox;
    }

    public void updateTopics() {
        printWriter.println(Message.getTopicsForClient);
        logSent(Message.getTopicsForClient);

        try {
            String response = bufferedReader.readLine();
            logReceived(response);

            String[] lists = response.trim().split("\\s+");
            List<String> unsubscribedTopics = Json.unserializeStrings(lists[0]);
            List<String> subscribedTopics = Json.unserializeStrings(lists[1]);

            unsubscribedTopics.removeAll(subscribedTopics);

            topicToSubscribeChoiceBox.getItems().clear();
            topicToSubscribeChoiceBox.getItems().addAll(unsubscribedTopics);

            topicToUnsubscribeChoiceBox.getItems().clear();
            topicToUnsubscribeChoiceBox.getItems().addAll(subscribedTopics);
        } catch (Exception exception) {
            logException(exception);
        }
    }

    public String registerMessageInbox(String host, int port) throws IOException {
        String registerMessageInboxMessage = Message.registerMessageInbox + " " + Json.serializeStrings(host, String.valueOf(port));

        printWriter.println(registerMessageInboxMessage);
        logSent(registerMessageInboxMessage);

        String result = bufferedReader.readLine();
        logReceived(result);

        return result;
    }

    public String subscribeToTopic(String topic) throws IOException {
        if (topic.trim().isEmpty()) {
            return Message.subscribeToTopicEmptyError;
        }

        String subscribeToTopicMessage = Message.subscribeToTopic + " " + Json.serializeString(topic);

        printWriter.println(subscribeToTopicMessage);
        logSent(subscribeToTopicMessage);

        String result = bufferedReader.readLine();
        logReceived(result);

        return result;
    }

    public String unsubscribeFromTopic(String topic) throws IOException {
        if (topic.trim().isEmpty()) {
            return Message.unsubscribeFromTopicEmptyError;
        }

        String unsubscribeFromTopicMessage = Message.unsubscribeFromTopic + " " + Json.serializeString(topic);

        printWriter.println(unsubscribeFromTopicMessage);
        logSent(unsubscribeFromTopicMessage);

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
