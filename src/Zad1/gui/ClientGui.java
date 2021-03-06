package Zad1.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Zad1.gui.controller.ClientGuiController;
import Zad1.socket.server.MessageInboxServer;
import Zad1.socket.service.ClientService;

import java.net.URL;

public class ClientGui extends Application {
    private static ClientGuiController clientGuiController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("file:///C:/Users/wojci/IdeaProjects/TPO3_FW_S21471/src/zad1/gui/template/client-gui.fxml"));
        loader.setController(clientGuiController);
        VBox vbox = loader.load();

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();
    }

    public void initialize(ClientService clientService, MessageInboxServer messageInboxServer) {
        clientGuiController = new ClientGuiController();
        clientGuiController.setClientService(clientService);
        clientGuiController.setMessageInboxServer(messageInboxServer);
        launch();
    }

    @Override
    public void stop() {
        clientGuiController.closeResources();
    }
}
