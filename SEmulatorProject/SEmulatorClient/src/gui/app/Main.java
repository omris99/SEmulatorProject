package gui.app;

import gui.app.resources.themes.Theme;
import http.HttpClientUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    private static Scene scene;
    private ClientController clientController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(750);
        primaryStage.setTitle("S-Emulator");

        URL client = getClass().getResource("Client.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(client);
            Parent root = fxmlLoader.load();
            clientController = fxmlLoader.getController();

            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/app/resources/images/icon.png")));
            primaryStage.getIcons().add(icon);

            scene = new Scene(root, 1540, 750);
            applyTheme(Theme.CLASSIC);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void applyTheme(Theme theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource(theme.getCssFilePath())).toExternalForm());
    }

    @Override
    public void stop() throws Exception {
        clientController.close();
        HttpClientUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
