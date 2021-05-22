package zad1.socket.service;

import zad1.constant.Message;
import zad1.serialization.Json;

import java.io.IOException;

public class ClientService extends TopicService {
    public ClientService(String host, int port) {
        super(host, port);
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
        String subscribeToTopicMessage = Message.subscribeToTopic + " " + Json.serializeString(topic);

        printWriter.println(subscribeToTopicMessage);
        logSent(subscribeToTopicMessage);

        String result = bufferedReader.readLine();
        logReceived(result);

        return result;
    }

    public String unsubscribeFromTopic(String topic) throws IOException {
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
