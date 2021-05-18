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

    public void onCreateButtonClicked(Event e) {
        topicToCreate.setText("Created");
    }

    public void onDeleteButtonClicked(Event e) {
        topicToDelete.setValue("Deleted");
    }

    public void onPublishButtonClicked(Event e) {
        topicToPublish.setValue("Published");
        messageToPublish.clear();
    }
}
