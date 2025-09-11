package gui.components.expandationlevelwindow;

import gui.components.toolbar.InstructionsWindowToolbarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ExpandationLevelWindowController {

    private InstructionsWindowToolbarController instructionsWindowToolbarController;

    @FXML
    private ChoiceBox<Integer> currentDegreeChoiceBox;
    @FXML
    private VBox expandationLevelWindow;

    @FXML
    private Label maximalDegreeLabel;

    @FXML
    public void initialize() {
        currentDegreeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                return;
            }

            instructionsWindowToolbarController.onDegreeChoice(newVal);
        });
    }

    public void setMaximalDegree(int maximalDegree) {
        currentDegreeChoiceBox.getItems().clear();
        for (int i = 0; i <= maximalDegree; i++) {
            currentDegreeChoiceBox.getItems().add(i);
        }

        maximalDegreeLabel.setText(String.valueOf(maximalDegree));
    }

    public void setInstructionsWindowToolbarController(InstructionsWindowToolbarController instructionsWindowToolbarController) {
        this.instructionsWindowToolbarController = instructionsWindowToolbarController;
    }

    public void setDegree(int degree) {
        currentDegreeChoiceBox.setValue(degree);
//        currentDegreeField.setText(String.valueOf(currentExpandationLevel));
    }

    public void onProgramLoaded(int maximalDegree) {
        currentDegreeChoiceBox.getItems().clear();
        for (int i = 0; i <= maximalDegree; i++) {
            currentDegreeChoiceBox.getItems().add(i);
        }

        maximalDegreeLabel.setText(String.valueOf(maximalDegree));

    }

}
