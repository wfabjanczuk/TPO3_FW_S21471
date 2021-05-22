package zad1.gui.controller;

import javafx.event.Event;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import zad1.socket.server.MessageInboxServer;
import zad1.socket.service.ClientService;

import java.util.List;

public class ClientGuiController {
    private ClientService clientService;
    private MessageInboxServer messageInboxServer;

    public ChoiceBox<String> topicToSubscribe;
    public ChoiceBox<String> topicToUnsubscribe;
    public TextArea messages;
    public Text subscribeResult;
    public Text unsubscribeResult;

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setMessageInboxServer(MessageInboxServer messageInboxServer) {
        this.messageInboxServer = messageInboxServer;
        this.messageInboxServer.setMessagesTextArea(messages);
    }

    private void registerMessageInbox() {
        String result;
        try {
            result = clientService.registerMessageInbox(
                    messageInboxServer.getHost(),
                    messageInboxServer.getPort()
            );
        } catch (Exception exception) {
            result = "Error in registering message inbox";
        }
        messages.appendText(result);
    }

    public void initialize() {
        registerMessageInbox();
        refreshTopics();
    }

    public void onRefreshClicked(Event e) {
        refreshTopics();
    }

    private void refreshTopics() {
        List<String> topicsList = clientService.getTopics();

        topicToSubscribe.getItems().clear();
        topicToSubscribe.getItems().addAll(topicsList);

        topicToUnsubscribe.getItems().clear();
        topicToUnsubscribe.getItems().addAll(topicsList);
    }

    private void clearResults() {
        subscribeResult.setText(null);
        unsubscribeResult.setText(null);
    }

    public void onSubscribeClicked(Event e) {
        clearResults();

        String result;
        try {
            result = clientService.subscribeToTopic(topicToSubscribe.getValue());
        } catch (Exception exception) {
            result = "Error in subscribing to topic";
        }

        subscribeResult.setText(result);
        refreshTopics();
    }

    public void onUnsubscribeClicked(Event e) {
        clearResults();

        String result;
        try {
            result = clientService.subscribeToTopic(topicToUnsubscribe.getValue());
        } catch (Exception exception) {
            result = "Error in unsubscribing from topic";
        }

        unsubscribeResult.setText(result);
        refreshTopics();
    }

    public void closeResources() {
        clientService.closeResources();
        messageInboxServer.closeResources();
    }
}
