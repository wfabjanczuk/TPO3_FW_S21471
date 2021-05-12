package zad1;

import zad1.constant.Message;
import zad1.serialization.Json;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server extends Thread implements LoggableThread {
    private static String[] topics = {"politics", "sport", "celebrities", "food", "technology"};
    private volatile boolean isServerRunning;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private static Charset charset = StandardCharsets.UTF_8;
    private static final int bufferSize = 1024;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

    public Server(String host, int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(host, port));

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception exception) {
            logThreadException(exception);
            logThreadCannotInitialize();
            logThreadCannotStart();

            System.exit(1);
        }

        logThreadInitialized();
    }

    public void run() {
        logThreadStarted();
        isServerRunning = true;

        while (isServerRunning) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeysIterator = selectionKeys.iterator();

                while (selectionKeysIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeysIterator.next();
                    selectionKeysIterator.remove();

                    if (!serveSelectionKey(selectionKey)) {
                        throw new Exception("SelectionKey could not be served.");
                    }
                }
            } catch (Exception exception) {
                logThreadException(exception);
            }
        }
    }

    public void stopServer() {
        isServerRunning = false;
    }

    private boolean serveSelectionKey(SelectionKey selectionKey) throws Exception {
        if (selectionKey.isAcceptable()) {
            return executeAccept();
        }

        if (selectionKey.isReadable()) {
            return executeRead(selectionKey);
        }

        return false;
    }

    private boolean executeAccept() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        logThreadAcceptedConnection();

        return true;
    }

    private boolean executeRead(SelectionKey selectionKey) throws Exception {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        if (!socketChannel.isOpen()) {
            return false;
        }

        String message = readLine(socketChannel);
        logThreadReceived(message);

        return handleMessage(message, socketChannel);
    }

    private String readLine(SocketChannel socketChannel) {
        byteBuffer.clear();
        StringBuilder requestStringBuilder = new StringBuilder();

        try {
            while (socketChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                CharBuffer charBuffer = charset.decode(byteBuffer);

                while (charBuffer.hasRemaining()) {
                    char c = charBuffer.get();
                    if (isEndlineChar(c)) {
                        return requestStringBuilder.toString();
                    }
                    requestStringBuilder.append(c);
                }
            }
        } catch (Exception exception) {
            logThreadException(exception);
        }

        return requestStringBuilder.toString();
    }

    private boolean isEndlineChar(char c) {
        return c == '\r' || c == '\n';
    }

    private boolean handleMessage(String message, SocketChannel socketChannel) throws Exception {
        if (message.trim().isEmpty()) {
            socketChannel.socket().close();
            logThreadChannelClosed();
            throw new Exception("Empty message received.");
        }

        return handleMessageByParts(message.split("\\s+"), socketChannel);
    }

    private boolean handleMessageByParts(String[] messageParts, SocketChannel socketChannel) throws IOException {
        String messageType = messageParts[0];

        if (messageType.equals(Message.goodbye)) {
            return handleGoodbyeMessage(socketChannel);
        }

        if (messageType.equals(Message.getTopics)) {
            return handleGetTopicsMessage(socketChannel);
        }

        if (messageType.equals(Message.setTopics)) {
            return handleSetTopicsMessage(messageParts, socketChannel);
        }

        return true;
    }

    private boolean handleGoodbyeMessage(SocketChannel socketChannel) throws IOException {
        socketChannel.socket().close();
        logThreadChannelClosed();
        return true;
    }

    private boolean handleGetTopicsMessage(SocketChannel socketChannel) throws IOException {
        String response = Json.serializeArrayOfStrings(topics) + '\n';
        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(response));
        socketChannel.write(responseByteBuffer);

        logThreadSent(response);
        return true;
    }

    private boolean handleSetTopicsMessage(String[] messageParts, SocketChannel socketChannel) throws IOException {
        topics = Json.unserializeArrayOfStrings(messageParts[1]);
        ByteBuffer responseByteBuffer = charset.encode(CharBuffer.wrap(Message.setTopicsResponse + '\n'));
        socketChannel.write(responseByteBuffer);

        logThreadSent(Message.setTopicsResponse);
        return true;
    }
}
