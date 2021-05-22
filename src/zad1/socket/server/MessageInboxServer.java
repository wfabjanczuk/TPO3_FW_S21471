package zad1.socket.server;

import javafx.scene.control.TextArea;
import zad1.constant.Message;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class MessageInboxServer extends SocketChannelServer {
    private TextArea messagesTextArea;

    public MessageInboxServer(String host, int port) {
        super(host, port);
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
            return handlePublish(messageParts);
        }

        return false;
    }

    private boolean handlePublish(String[] messageParts) {
        messagesTextArea.appendText(messageParts[1] + "\n");
        return true;
    }

    private boolean handleGoodbyeFromMainServerMessage(SocketChannel socketChannel) throws IOException {
        socketChannel.socket().close();
        logConnectionClosed();
        return true;
    }
}
