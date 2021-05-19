package zad1.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import zad1.gui.controller.AdminGuiController;
import zad1.service.AdminService;

import java.net.URL;

public class AdminGui extends Application {
    private static AdminGuiController adminGuiController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (adminGuiController == null) {
            throw new Exception("AdminGuiController has not been set.");
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("file:///C:/Users/wojci/IdeaProjects/TPO3_FW_S21471/src/zad1/gui/template/admin-gui.fxml"));
        loader.setController(adminGuiController);
        VBox vbox = loader.load();

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin");
        primaryStage.show();
    }

    public void initialize(AdminService adminService) {
        adminGuiController = new AdminGuiController();
        adminGuiController.setAdminService(adminService);
        launch();
    }
}
