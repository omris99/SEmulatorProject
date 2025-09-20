package gui.app;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.util.Objects;

public class Main extends Application {
    private static Scene scene;

    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        primaryStage.setTitle("S-Emulator");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/app/resources/images/icon.png")));
        primaryStage.getIcons().add(icon);

        Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("App.fxml")));
        scene = new Scene(load, 1540, 720);
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        applyTheme(Theme.CLASSIC);


        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void applyTheme(Theme theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("app.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource(theme.getCssFilePath())).toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
