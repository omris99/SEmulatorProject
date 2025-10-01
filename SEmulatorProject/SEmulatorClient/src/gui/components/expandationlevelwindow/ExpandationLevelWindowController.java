package gui.components.expandationlevelwindow;

import gui.components.toolbar.InstructionsWindowToolbarController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class ExpandationLevelWindowController {

    private InstructionsWindowToolbarController instructionsWindowToolbarController;

    @FXML
    private ChoiceBox<Integer> currentDegreeChoiceBox;

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

    public void setInstructionsWindowToolbarController(InstructionsWindowToolbarController instructionsWindowToolbarController) {
        this.instructionsWindowToolbarController = instructionsWindowToolbarController;
    }

    public void updateExpandationLevelChoiceBoxAndMaximalDegree(int maximalDegree) {
        currentDegreeChoiceBox.getItems().clear();
        currentDegreeChoiceBox.setValue(0);
        for (int i = 0; i <= maximalDegree; i++) {
            currentDegreeChoiceBox.getItems().add(i);
        }

        maximalDegreeLabel.setText(String.valueOf(maximalDegree));
    }

    public int getCurrentDegree() {
        Integer value = currentDegreeChoiceBox.getValue();
        return value == null ? 0 : value;
    }

    public void setDegree(int degree) {
        currentDegreeChoiceBox.setValue(degree);
    }

    public int getMaximalDegree() {
        String text = maximalDegreeLabel.getText();
        return Integer.parseInt(text);
    }

    public void disableDegreeChoiceBox(boolean disable) {
        currentDegreeChoiceBox.setDisable(disable);
    }
}
