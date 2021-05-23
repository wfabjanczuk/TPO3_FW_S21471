package zad1.socket.service;

import zad1.constant.Message;

import java.io.IOException;

public class SubscriptionService extends Service {
    public SubscriptionService(String host, int port) {
        super(host, port);
    }

    public String forwardMessage(String fullMessage) throws IOException {
        printWriter.println(fullMessage);
        logSent(fullMessage);

        String result = bufferedReader.readLine();
        logReceived(result);

        return result;
    }

    @Override
    protected void sayGoodbye() {
        try {
            printWriter.println(Message.goodbyeFromMainServer);
            logSent(Message.goodbyeFromMainServer);
        } catch (Exception exception) {
            logException(exception);
        }
    }
}
