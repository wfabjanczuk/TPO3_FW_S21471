package zad1;

import zad1.constant.Message;
import zad1.serialization.Json;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainServer extends SocketChannelServer {
    private static List<String> topics = new ArrayList<>(Arrays.asList("politics", "sport", "celebrities", "food"));

    public MainServer(String host, int port) {
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

        if (messageType.equals(Message.deleteTopic)) {
            return handleDeleteTopicMessage(messageParts, socketChannel);
        }

        if (messageType.equals(Message.publish)) {
            return handlePublishMessage(messageParts, socketChannel);
        }

        return true;
    }

    private boolean handleGoodbyeMessage(SocketChannel socketChannel) throws IOException {
        socketChannel.socket().close();
        logChannelClosed();
        return true;
    }

    private boolean handleGetTopicsMessage(SocketChannel socketChannel) throws IOException {
        String response = Json.serializeStrings(topics) + '\n';
        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response));
        socketChannel.write(responseByteBuffer);

        logSent(response);
        return true;
    }

    private boolean handleAddTopicMessage(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String topic = Json.unserializeString(messageParts[1]);
        String response = addTopic(topic);

        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response + '\n'));
        socketChannel.write(responseByteBuffer);

        logSent(response);
        return true;
    }

    private String addTopic(String topic) {
        if (topic.trim().isEmpty()) {
            return Message.addTopicEmptyError;
        }

        if (topics.contains(topic)) {
            return Message.addTopicResponseError;
        }

        topics.add(topic);
        return Message.addTopicResponseSuccess;
    }

    private boolean handleDeleteTopicMessage(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String topic = Json.unserializeString(messageParts[1]);
        String response = deleteTopic(topic);

        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response + '\n'));
        socketChannel.write(responseByteBuffer);

        logSent(response);
        return true;
    }

    private String deleteTopic(String topic) {
        if (topics.contains(topic)) {
            topics.remove(topic);
            return Message.deleteTopicResponseSuccess;
        }

        return Message.deleteTopicResponseError;
    }

    private boolean handlePublishMessage(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String response = Message.publishResponseSuccess;

        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response + '\n'));
        socketChannel.write(responseByteBuffer);

        logSent(response);
        return true;
    }
}
