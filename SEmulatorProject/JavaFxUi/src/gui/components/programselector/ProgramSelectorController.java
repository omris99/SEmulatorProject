package gui.components.programselector;

import dto.ProgramDTO;
import gui.components.toolbar.InstructionsWindowToolbarController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import logic.model.functionsrepo.FunctionsRepo;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        programOptions.addAll(program.getFunctionsNames().stream().map(name -> FunctionsRepo.getInstance().getFunctionUserString(name)).toList());
        programChoiceBox.getSelectionModel().clearSelection();
        programChoiceBox.getItems().clear();
        programChoiceBox.getItems().addAll(programOptions);
        programChoiceBox.getSelectionModel().select(0);
    }
}
