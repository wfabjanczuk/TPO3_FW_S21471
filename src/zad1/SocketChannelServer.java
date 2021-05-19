package zad1;

import zad1.service.Loggable;

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

abstract public class SocketChannelServer extends Thread implements Loggable {
    abstract protected boolean handleMessageByParts(String[] messageParts, SocketChannel socketChannel) throws IOException;

    protected static Charset charset = StandardCharsets.UTF_8;
    private static final int bufferSize = 1024;

    private volatile boolean isServerRunning;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

    public SocketChannelServer(String host, int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(host, port));

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception exception) {
            logException(exception);
            logCannotInitialize();
            logCannotStart();

            System.exit(1);
        }

        logInitialized();
    }

    public void run() {
        logStarted();
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
                logException(exception);
            }
        }
    }

    public void stopServer() {
        isServerRunning = false;
    }

    protected boolean serveSelectionKey(SelectionKey selectionKey) throws Exception {
        if (selectionKey.isAcceptable()) {
            return executeAccept();
        }

        if (selectionKey.isReadable()) {
            return executeRead(selectionKey);
        }

        if (selectionKey.isWritable()) {
            return executeWrite(selectionKey);
        }

        return false;
    }

    protected boolean executeAccept() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        logAcceptedConnection();

        return true;
    }

    protected boolean executeRead(SelectionKey selectionKey) throws Exception {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        if (!socketChannel.isOpen()) {
            return false;
        }

        String message = readLine(socketChannel);
        logReceived(message);

        return handleMessage(message, socketChannel);
    }

    protected String readLine(SocketChannel socketChannel) {
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
            logException(exception);
        }

        return requestStringBuilder.toString();
    }

    protected boolean isEndlineChar(char c) {
        return c == '\r' || c == '\n';
    }

    protected boolean handleMessage(String message, SocketChannel socketChannel) throws Exception {
        if (message.trim().isEmpty()) {
            socketChannel.socket().close();
            logChannelClosed();
            throw new Exception("Empty message received.");
        }

        return handleMessageByParts(message.split("\\s+"), socketChannel);
    }

    protected boolean executeWrite(SelectionKey selectionKey) {
        return true;
    }
}
