package gui.components.highlightselection;

import gui.components.toolbar.InstructionsWindowToolbarController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.List;

public class HighlightSelectionController {

    @FXML
    private InstructionsWindowToolbarController instructionsWindowToolbarController;

    @FXML
    private ComboBox<String> highlightSelector;

    @FXML
    public void initialize() {
        highlightSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                return;
            }

            instructionsWindowToolbarController.onHighlightSelectionChange(newVal);
        });
    }

    public void updateOptions(List<String> highlightOptions) {
        highlightSelector.getItems().clear();
        highlightSelector.getItems().addAll(highlightOptions);
    }

    public void setInstructionsWindowToolbarController(InstructionsWindowToolbarController instructionsWindowToolbarController) {
        this.instructionsWindowToolbarController = instructionsWindowToolbarController;
    }

    public void resetSelection() {
        highlightSelector.getSelectionModel().clearSelection();
        highlightSelector.setPromptText("Select Label/Variable");
    }
}
