package gui.components.programselector;

import clientserverdto.ProgramDTO;
import gui.components.toolbar.InstructionsWindowToolbarController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.LinkedList;
import java.util.List;

public class ProgramSelectorController {
    @FXML
    private InstructionsWindowToolbarController instructionsWindowToolbarController;

    @FXML
    private ChoiceBox<String> programChoiceBox;
    @FXML
    public void initialize() {
        programChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            instructionsWindowToolbarController.onProgramSelectorChange(newVal);
        });
    }

    public void setInstructionsWindowToolbarController(InstructionsWindowToolbarController instructionsWindowToolbarController) {
        this.instructionsWindowToolbarController = instructionsWindowToolbarController;
    }

    public void updateOptions(ProgramDTO program) {
        List<String> programOptions = new LinkedList<>();
        programOptions.add(program.getName());
        programOptions.addAll(program.getFunctionsNames());
        programChoiceBox.getSelectionModel().clearSelection();
        programChoiceBox.getItems().clear();
        programChoiceBox.getItems().addAll(programOptions);
        programChoiceBox.getSelectionModel().select(0);
    }
}
