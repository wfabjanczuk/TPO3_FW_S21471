package zad1.service;

import zad1.constant.Message;
import zad1.serialization.Json;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server extends SocketChannelServer {
    private static List<String> topics = new ArrayList<>(Arrays.asList("politics", "sport", "celebrities", "food"));

    public Server(String host, int port) {
        super(host, port);
    }

    protected boolean handleMessageByParts(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String messageType = messageParts[0];

        if (messageType.equals(Message.goodbye)) {
            return handleGoodbyeMessage(socketChannel);
        }

        if (messageType.equals(Message.getTopics)) {
            return handleGetTopicsMessage(socketChannel);
        }

        if (messageType.equals(Message.addTopic)) {
            return handleAddTopicMessage(messageParts, socketChannel);
        }

        if (messageType.equals(Message.removeTopic)) {
            return handleRemoveTopicMessage(messageParts, socketChannel);
        }

        return true;
    }

    private boolean handleGoodbyeMessage(SocketChannel socketChannel) throws IOException {
        socketChannel.socket().close();
        logThreadChannelClosed();
        return true;
    }

    private boolean handleGetTopicsMessage(SocketChannel socketChannel) throws IOException {
        String response = Json.serializeStrings(topics) + '\n';
        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response));
        socketChannel.write(responseByteBuffer);

        logThreadSent(response);
        return true;
    }

    private boolean handleAddTopicMessage(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String topic = Json.unserializeString(messageParts[1]);
        String response = addTopic(topic)
                ? Message.addTopicResponseSuccess
                : Message.addTopicResponseError;

        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response + '\n'));
        socketChannel.write(responseByteBuffer);

        logThreadSent(Message.addTopicResponseSuccess);
        return true;
    }

    private boolean addTopic(String topic) {
        if (topics.contains(topic)) {
            return false;
        }

        return topics.add(topic);
    }

    private boolean handleRemoveTopicMessage(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String topic = Json.unserializeString(messageParts[1]);
        String response = removeTopic(topic)
                ? Message.removeTopicResponseSuccess
                : Message.removeTopicResponseError;

        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response + '\n'));
        socketChannel.write(responseByteBuffer);

        logThreadSent(Message.addTopicResponseSuccess);
        return true;
    }

    private boolean removeTopic(String topic) {
        if (topics.contains(topic)) {
            return topics.remove(topic);
        }

        return false;
    }
}
