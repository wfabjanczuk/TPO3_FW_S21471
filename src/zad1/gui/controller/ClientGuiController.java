package zad1.gui.controller;

import javafx.event.Event;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import zad1.socket.server.MessageInboxServer;

public class ClientGuiController {
    private MessageInboxServer messageInboxServer;

    public ChoiceBox<String> topicToSubscribe;
    public ChoiceBox<String> topicToUnsubscribe;
    public TextArea messages;

    public void setMessageInboxServer(MessageInboxServer messageInboxServer) {
        this.messageInboxServer = messageInboxServer;
        this.messageInboxServer.setMessagesTextArea(messages);
    }

    public void onSubscribeClicked(Event e) {
        messages.appendText("Subscribed\n");
    }

    public void onUnsubscribeClicked(Event e) {
        messages.appendText("Unsubscribed\n");
    }
}
