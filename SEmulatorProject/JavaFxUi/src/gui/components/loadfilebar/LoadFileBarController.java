package gui.components.loadfilebar;

import gui.app.AppController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import logic.engine.EmulatorEngine;
import logic.exceptions.InvalidXmlFileException;

import jakarta.xml.bind.JAXBException;
import java.io.File;

public class LoadFileBarController {
    private AppController appController;

    @FXML
    private TextField FilePathTextField;

    @FXML
    private Button LoadFileButton;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void LoadFileButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML Program File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(LoadFileButton.getScene().getWindow());

        if (selectedFile == null) {
            return;
        }

        Task<Void> loadFileTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    appController.loadProgram(selectedFile.getAbsolutePath());
//                    engine.loadProgram(selectedFile.getAbsolutePath());
                    updateMessage(selectedFile.getAbsolutePath());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    updateMessage(e.getMessage());
                }

                return null;
            }
        };

        FilePathTextField.textProperty().bind(loadFileTask.messageProperty());

        new Thread(loadFileTask).start();
    }
}
