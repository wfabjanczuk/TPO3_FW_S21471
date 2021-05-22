package zad1.gui.controller;

import javafx.event.Event;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import zad1.socket.server.MessageInboxServer;
import zad1.socket.service.ClientService;

import java.util.List;

public class ClientGuiController {
    private ClientService clientService;
    private MessageInboxServer messageInboxServer;

    public ChoiceBox<String> topicToSubscribe;
    public ChoiceBox<String> topicToUnsubscribe;
    public TextArea messages;

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setMessageInboxServer(MessageInboxServer messageInboxServer) {
        this.messageInboxServer = messageInboxServer;
        this.messageInboxServer.setMessagesTextArea(messages);
    }

    public void initialize() {
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

    public void onSubscribeClicked(Event e) {
        messages.appendText("Subscribed\n");
    }

    public void onUnsubscribeClicked(Event e) {
        messages.appendText("Unsubscribed\n");
    }

    public void closeResources() {
        messageInboxServer.closeResources();
    }
}
