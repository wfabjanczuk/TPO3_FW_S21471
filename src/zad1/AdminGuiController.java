package zad1;

import javafx.event.Event;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdminGuiController {
    public TextField topicToCreate;
    public ChoiceBox<String> topicToDelete;
    public ChoiceBox<String> topicToPublish;
    public TextArea messageToPublish;

    public void onCreateClicked(Event e) {
        messageToPublish.setText("Created");
    }

    public void onDeleteClicked(Event e) {
        messageToPublish.setText("Deleted");
    }

    public void onPublishClicked(Event e) {
        messageToPublish.setText("Published");
    }
}
