package zad1.gui.controller;

import javafx.event.Event;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import zad1.socket.service.AdminService;

import java.util.List;

public class AdminGuiController {
    private AdminService adminService;

    public TextField topicToCreate;
    public ChoiceBox<String> topicToDelete;
    public ChoiceBox<String> topicToPublish;
    public TextArea messageToPublish;
    public TextArea topics;
    public Text createResult;
    public Text deleteResult;
    public Text publishResult;

    public void initialize() {
        refreshTopics();
    }

    private void refreshTopics() {
        List<String> topicsList = adminService.getTopics();
        topics.setText(String.join("\n", topicsList));

        topicToDelete.getItems().clear();
        topicToDelete.getItems().addAll(topicsList);

        topicToPublish.getItems().clear();
        topicToPublish.getItems().addAll(topicsList);
    }

    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    public void onCreateClicked(Event e) {
        String result;
        try {
            result = adminService.addTopic(topicToCreate.getText());
        } catch (Exception exception) {
            result = "Error in creating the topic";
        }

        createResult.setText(result);
        refreshTopics();
    }

    public void onDeleteClicked(Event e) {
        String result;
        try {
            result = adminService.deleteTopic(topicToDelete.getValue());
        } catch (Exception exception) {
            result = "Error in deleting the topic";
        }

        deleteResult.setText(result);
        refreshTopics();
    }

    public void onPublishClicked(Event e) {
        String result;
        try {
            result = adminService.publishMessage(messageToPublish.getText(), topicToPublish.getValue());
        } catch (Exception exception) {
            result = "Error in publishing the message";
        }

        publishResult.setText(result);
        refreshTopics();
    }

    public void closeResources() {
        adminService.closeResources();
    }
}
