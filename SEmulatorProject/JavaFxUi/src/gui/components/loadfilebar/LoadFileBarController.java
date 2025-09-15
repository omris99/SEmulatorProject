package gui.components.loadfilebar;

import gui.app.AppController;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;

public class LoadFileBarController {
    private AppController appController;

    @FXML
    private TextField FilePathTextField;

    @FXML
    private Button LoadFileButton;

    @FXML
    private ProgressBar progressBar;


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

        appController.loadProgramWithProgress(selectedFile);
    }

    public void bindTaskToUI(Task<?> task) {
        progressBar.progressProperty().bind(task.progressProperty());
        FilePathTextField.textProperty().bind(task.messageProperty());
    }

    public void setProgressBarLoadErrorStyle() {
        progressBar.getStyleClass().add("load-error");
    }

    public void removeProgressBarErrorStyle() {
        progressBar.getStyleClass().remove("load-error");
    }

}
