package zad1.socket.service;

import zad1.socket.Loggable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

abstract public class TopicService implements Loggable {
    abstract protected void sayGoodbye();

    protected Socket socket;
    protected PrintWriter printWriter;
    protected BufferedReader bufferedReader;

    public TopicService(String host, int port) {
        try {
            socket = new Socket(host, port);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception exception) {
            logException(exception);
            System.exit(1);
        }
    }

    public void closeResources() {
        try {
            if (socket != null) {
                sayGoodbye();
                socket.close();
            }

            logConnectionResourcesClosed();
            logConnectionClosed();
        } catch (IOException exception) {
            logException(exception);
        }
    }
}
