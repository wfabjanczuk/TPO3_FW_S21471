package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server extends Thread implements LoggableThread {
    private static String[] topics = {"politics", "sport", "celebrities", "food", "technology"};
    private volatile boolean isServerRunning;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;


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

    private boolean serveSelectionKey(SelectionKey selectionKey) throws IOException {
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

    private boolean executeAccept() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        logThreadAcceptedConnection();

        return true;
    }

    private boolean executeRead(SelectionKey selectionKey) {
        return true;
    }

    private boolean executeWrite(SelectionKey selectionKey) {
        return true;
    }
}
