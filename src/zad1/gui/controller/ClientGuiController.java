package zad1.gui.controller;

import javafx.event.Event;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class ClientGuiController {
    public ChoiceBox<String> topicToSubscribe;
    public ChoiceBox<String> topicToUnsubscribe;
    public TextArea messages;

    public void onSubscribeClicked(Event e) {
        messages.appendText("Subscribed\n");
    }

    public void onUnsubscribeClicked(Event e) {
        messages.appendText("Unsubscribed\n");
    }
}
