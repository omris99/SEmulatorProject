package gui.components.loadfilebar;

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

    @FXML
    private TextField FilePathTextField;

    @FXML
    private Button LoadFileButton;

    private EmulatorEngine engine = new EmulatorEngine();

    public void setEngine(EmulatorEngine engine) {
        this.engine = engine;
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
                    engine.loadProgram(selectedFile.getAbsolutePath());
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
