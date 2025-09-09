package ui.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        primaryStage.setTitle("S-Emulator");

        Parent load = FXMLLoader.load(getClass().getResource("App.fxml"));
        Scene scene = new Scene(load, 1259, 765);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
