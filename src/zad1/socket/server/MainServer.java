package zad1.socket.server;

import zad1.constant.Message;
import zad1.constant.Topic;
import zad1.serialization.Json;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.stream.Collectors;

public class MainServer extends SocketChannelServer {
    private static final List<String> topics = new LinkedList<>(Arrays.asList(Topic.defaultTopics));

    private static final Map<String, List<String>> topicsInboxesMap = new LinkedHashMap<>();
    private static final Map<String, Socket> inboxesSocketsMap = new LinkedHashMap<>();
    private static final Map<String, String> clientsInboxesMap = new LinkedHashMap<>();

    public MainServer(String host, int port) {
        super(host, port);
    }

    @Override
    protected boolean handleMessageByParts(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String messageType = messageParts[0];

        if (messageType.equals(Message.goodbyeFromAdmin)) {
            return handleGoodbyeFromAdminMessage(socketChannel);
        }

        if (messageType.equals(Message.goodbyeFromClient)) {
            return handleGoodbyeFromClientMessage(socketChannel);
        }

        if (messageType.equals(Message.getTopicsForAdmin)) {
            return handleGetTopicsForAdminMessage(socketChannel);
        }

        if (messageType.equals(Message.getTopicsForClient)) {
            return handleGetTopicsForClientMessage(socketChannel);
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

        if (messageType.equals(Message.registerMessageInbox)) {
            return handleRegisterMessageInbox(messageParts, socketChannel);
        }

        if (messageType.equals(Message.subscribeToTopic)) {
            return handleSubscribe(messageParts, socketChannel);
        }

        if (messageType.equals(Message.unsubscribeFromTopic)) {
            return handleUnsubscribe(messageParts, socketChannel);
        }

        return false;
    }

    private boolean handleGoodbyeFromAdminMessage(SocketChannel socketChannel) throws IOException {
        socketChannel.socket().close();
        logConnectionClosed();
        return true;
    }

    private boolean handleGoodbyeFromClientMessage(SocketChannel socketChannel) throws IOException {
        String clientServiceConnectionString = getClientConnectionString(socketChannel);
        String messageInboxConnectionString = clientsInboxesMap.get(clientServiceConnectionString);

        removeInboxFromTopics(messageInboxConnectionString);
        inboxesSocketsMap.remove(messageInboxConnectionString);
        clientsInboxesMap.remove(clientServiceConnectionString);

        socketChannel.socket().close();
        logConnectionClosed();
        return true;
    }

    private void removeInboxFromTopics(String messageInboxConnectionString) {
        List<String> topicsToRemove = new LinkedList<>();

        topicsInboxesMap.forEach((topic, inboxes) -> {
            inboxes.remove(messageInboxConnectionString);

            if (inboxes.isEmpty()) {
                topicsToRemove.add(topic);
            }
        });

        topicsToRemove.forEach(topicsInboxesMap::remove);
    }

    private boolean handleGetTopicsForAdminMessage(SocketChannel socketChannel) throws IOException {
        String response = Json.serializeStrings(topics) + '\n';
        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response));
        socketChannel.write(responseByteBuffer);

        logSent(response);
        return true;
    }

    private boolean handleGetTopicsForClientMessage(SocketChannel socketChannel) throws IOException {
        String messageInboxConnectionString = clientsInboxesMap.get(getClientConnectionString(socketChannel));

        List<String> clientTopics = topicsInboxesMap.entrySet().stream()
                .filter(e -> e.getValue().contains(messageInboxConnectionString))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        String response = Json.serializeStrings(topics.stream().filter(
                t -> !clientTopics.contains(t)
        ).collect(Collectors.toList())) + " " + Json.serializeStrings(clientTopics) + '\n';

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
            return Message.addTopicExistsError;
        }

        topics.add(topic);
        return Message.addTopicSuccess;
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
            topicsInboxesMap.remove(topic);
            return Message.deleteTopicSuccess;
        }

        return Message.deleteTopicNotExistsError;
    }

    private boolean handlePublishMessage(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String response = Message.publishSuccess;

        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response + '\n'));
        socketChannel.write(responseByteBuffer);

        logSent(response);
        return true;
    }

    private boolean handleRegisterMessageInbox(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String response = registerMessageInbox(messageParts, socketChannel);

        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response + '\n'));
        socketChannel.write(responseByteBuffer);

        logSent(response);
        return true;
    }

    private String registerMessageInbox(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String clientServiceConnectionString = getClientConnectionString(socketChannel);

        List<String> hostPort = Json.unserializeStrings(messageParts[1]);
        String messageInboxConnectionString = String.join(":", hostPort);

        clientsInboxesMap.put(clientServiceConnectionString, messageInboxConnectionString);
        inboxesSocketsMap.put(messageInboxConnectionString, new Socket(
                hostPort.get(0),
                Integer.parseInt(hostPort.get(1)))
        );

        return Message.registerMessageInboxSuccess;
    }

    private boolean handleSubscribe(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String response = subscribeToTopic(messageParts, socketChannel);

        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response + '\n'));
        socketChannel.write(responseByteBuffer);

        logSent(response);
        return true;
    }

    private String subscribeToTopic(String[] messageParts, SocketChannel socketChannel) {
        String topic = Json.unserializeString(messageParts[1]);

        if (topic.trim().isEmpty()) {
            return Message.subscribeToTopicEmptyError;
        }

        if (!topics.contains(topic)) {
            return Message.subscribeToTopicNotExistsError;
        }

        String messageInboxConnectionString = clientsInboxesMap.get(getClientConnectionString(socketChannel));

        if (topicsInboxesMap.get(topic) == null) {
            topicsInboxesMap.put(topic, new LinkedList<>(Collections.singleton(messageInboxConnectionString)));
        } else {
            List<String> topicInboxes = topicsInboxesMap.get(topic);

            if (topicInboxes.contains(messageInboxConnectionString)) {
                return Message.subscribeToTopicSubscribedError;
            }

            topicInboxes.add(messageInboxConnectionString);
        }

        return Message.subscribeToTopicSuccess;
    }

    private boolean handleUnsubscribe(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String response = unsubscribeFromTopic(messageParts, socketChannel);

        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response + '\n'));
        socketChannel.write(responseByteBuffer);

        logSent(response);
        return true;
    }

    private String unsubscribeFromTopic(String[] messageParts, SocketChannel socketChannel) {
        String topic = Json.unserializeString(messageParts[1]);

        if (topic.trim().isEmpty()) {
            return Message.unsubscribeFromTopicEmptyError;
        }

        if (!topics.contains(topic)) {
            return Message.unsubscribeFromTopicNotExistsError;
        }

        String messageInboxConnectionString = clientsInboxesMap.get(getClientConnectionString(socketChannel));

        if (topicsInboxesMap.get(topic) == null) {
            return Message.unsubscribeFromTopicNotSubscribedError;
        } else {
            List<String> topicInboxes = topicsInboxesMap.get(topic);

            if (!topicInboxes.contains(messageInboxConnectionString)) {
                return Message.unsubscribeFromTopicNotSubscribedError;
            }

            topicInboxes.remove(messageInboxConnectionString);
        }

        return Message.unsubscribeFromTopicSuccess;
    }

    private void showMaps() {
        for (Map.Entry<String, List<String>> topicsInboxesEntry : topicsInboxesMap.entrySet()) {
            System.out.println("\nInboxes for topic: " + topicsInboxesEntry.getKey());
            for (String inbox : topicsInboxesEntry.getValue()) {
                System.out.println(inbox);
            }
        }

        for (Map.Entry<String, Socket> inboxesSocketsEntry : inboxesSocketsMap.entrySet()) {
            System.out.println("\nSocket for inbox: " + inboxesSocketsEntry.getKey());
            System.out.println(inboxesSocketsEntry.getValue().getInetAddress().getHostAddress() + ":" + inboxesSocketsEntry.getValue().getPort());
        }

        for (Map.Entry<String, String> clientsInboxesEntry : clientsInboxesMap.entrySet()) {
            System.out.println("\nInbox for client: " + clientsInboxesEntry.getKey());
            System.out.println(clientsInboxesEntry.getValue());
        }
    }

    private String getClientConnectionString(SocketChannel socketChannel) {
        return socketChannel.socket().getInetAddress().getHostAddress() + ":" + socketChannel.socket().getPort();
    }
}
