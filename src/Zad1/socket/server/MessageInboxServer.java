package Zad1.socket.server;

import javafx.scene.control.TextArea;
import Zad1.constant.Message;
import Zad1.serialization.Json;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

public class MessageInboxServer extends SocketChannelServer {
    private final String host;
    private final int port;
    private TextArea messagesTextArea;

    public MessageInboxServer(String host, int port) {
        super(host, port);
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setMessagesTextArea(TextArea messagesTextArea) {
        this.messagesTextArea = messagesTextArea;
    }

    @Override
    protected boolean handleMessageByParts(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String messageType = messageParts[0];

        if (messageType.equals(Message.goodbyeFromMainServer)) {
            return handleGoodbyeFromMainServerMessage(socketChannel);
        }

        if (messageType.equals(Message.publish)) {
            return handlePublish(messageParts, socketChannel);
        }

        return false;
    }

    private boolean handlePublish(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String response = showReceivedMessage(messageParts);
        socketChannel.write(charset.encode(CharBuffer.wrap(response + "\n")));

        logSent(response);
        return true;
    }

    private String showReceivedMessage(String[] messageParts) {
        List<String> messageAndTopic = Json.unserializeStrings(messageParts[1]);

        String message = messageAndTopic.get(0);
        String topic = messageAndTopic.get(1);

        messagesTextArea.appendText(topic + ": " + message + "\n");

        return Message.publishReceived;
    }

    private boolean handleGoodbyeFromMainServerMessage(SocketChannel socketChannel) throws IOException {
        socketChannel.socket().close();
        logConnectionClosed();
        return true;
    }
}
