package gui.components.toolbar;

import dto.ProgramDTO;
import gui.components.expandationlevelwindow.ExpandationLevelWindowController;
import gui.components.highlightselection.HighlightSelectionController;
import gui.components.instructionswindow.InstructionsWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableType;

import java.util.ArrayList;
import java.util.List;

public class InstructionsWindowToolbarController {
    private InstructionsWindowController instructionsWindowController;

    @FXML
    private ExpandationLevelWindowController expandationLevelWindowController;

    @FXML
    private HighlightSelectionController highlightSelectionController;


    @FXML
    private Button expandButton;

    @FXML
    private Button collapseButton;

    @FXML
    public void initialize() {
        expandationLevelWindowController.setInstructionsWindowToolbarController(this);
        highlightSelectionController.setInstructionsWindowToolbarController(this);
    }

    public void onDegreeChoice(int newDegree) {
        instructionsWindowController.onDegreeChoice(newDegree);
    }

    public void setInstructionsWindowController(InstructionsWindowController instructionsWindowToolbarController) {
        this.instructionsWindowController = instructionsWindowToolbarController;
    }

    public void onProgramLoaded(ProgramDTO programDTO) {
        expandationLevelWindowController.updateExpandationLevelChoiceBoxAndMaximalDegree(programDTO.getMaximalDegree());
        updateHighlightOptions(programDTO);
    }

    public void onExpandButtonClick() {
        if (expandationLevelWindowController.getMaximalDegree() > expandationLevelWindowController.getCurrentDegree()) {
            onDegreeChoice(expandationLevelWindowController.getCurrentDegree() + 1);
            expandationLevelWindowController.setDegree(expandationLevelWindowController.getCurrentDegree() + 1);
        }
    }

    public void onCollapseButtonClick() {
        if (expandationLevelWindowController.getCurrentDegree() > 0) {
            onDegreeChoice(expandationLevelWindowController.getCurrentDegree() - 1);
            expandationLevelWindowController.setDegree(expandationLevelWindowController.getCurrentDegree() - 1);
        }
    }

    public void onHighlightSelectionChange(String selection) {
        instructionsWindowController.onHighlightSelectionChange(selection);
    }

    public void updateHighlightOptions(ProgramDTO programDTO) {
        List<String> highlightOptions = new ArrayList<>();
        highlightOptions.addAll(programDTO.getLabelsNames());
        highlightOptions.add("y");
        highlightOptions.addAll(programDTO.getInputNames());
        highlightOptions.addAll(programDTO.getWorkVariables().stream().map(Variable::getRepresentation).toList());
        highlightSelectionController.updateOptions(highlightOptions);
    }

    public int getDegreeChoice() {
        return expandationLevelWindowController.getCurrentDegree();
    }

    public void disableDegreeChoiceControls(boolean disable) {
        expandButton.setDisable(disable);
        collapseButton.setDisable(disable);
        expandationLevelWindowController.disableDegreeChoiceBox(disable);
    }
}
