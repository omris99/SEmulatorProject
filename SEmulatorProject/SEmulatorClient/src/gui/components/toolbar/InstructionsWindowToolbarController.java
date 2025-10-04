package gui.components.toolbar;

import dto.ProgramDTO;
import gui.components.expandationlevelwindow.ExpandationLevelWindowController;
import gui.components.highlightselection.HighlightSelectionController;
import gui.components.instructionswindow.InstructionsWindowController;
import gui.components.programselector.ProgramSelectorController;
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

//    @FXML
//    private ProgramSelectorController programSelectorController;

    @FXML
    private Button expandButton;

    @FXML
    private Button collapseButton;

    @FXML
    public void initialize() {
        expandationLevelWindowController.setInstructionsWindowToolbarController(this);
        highlightSelectionController.setInstructionsWindowToolbarController(this);
//        programSelectorController.setInstructionsWindowToolbarController(this);
    }

    public void onDegreeChoice(int newDegree) {
        instructionsWindowController.onDegreeChoice(newDegree);
    }

    public void setInstructionsWindowController(InstructionsWindowController instructionsWindowToolbarController) {
        this.instructionsWindowController = instructionsWindowToolbarController;
    }

    public void programChanged(ProgramDTO programDTO){
        expandationLevelWindowController.updateExpandationLevelChoiceBoxAndMaximalDegree(programDTO.getMaximalDegree());
        updateHighlightOptions(programDTO);
    }

    public void onProgramLoaded(ProgramDTO programDTO) {
        expandButton.setDisable(false);
        collapseButton.setDisable(false);
        expandationLevelWindowController.updateExpandationLevelChoiceBoxAndMaximalDegree(programDTO.getMaximalDegree());
        updateHighlightOptions(programDTO);
        updateProgramOptions(programDTO);
    }

    public void onExpandButtonClick() {
        if (expandationLevelWindowController.getMaximalDegree() > expandationLevelWindowController.getCurrentDegree()) {
            onDegreeChoice(expandationLevelWindowController.getCurrentDegree() + 1);
            expandationLevelWindowController.setDegree(expandationLevelWindowController.getCurrentDegree() + 1);
        }
    }

    public void setProgramDegree(int degree) {
        expandationLevelWindowController.setDegree(degree);
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

    public void onProgramSelectorChange(String selection) {
        instructionsWindowController.onProgramSelectorChange(selection);
    }

    public void updateHighlightOptions(ProgramDTO programDTO) {
        List<String> highlightOptions = new ArrayList<>();
        highlightOptions.addAll(programDTO.getLabelsNames());
        highlightOptions.add("y");
        highlightOptions.addAll(programDTO.getInputNames());
        highlightOptions.addAll(programDTO.getWorkVariables());
        highlightSelectionController.updateOptions(highlightOptions);
    }

    public void updateProgramOptions(ProgramDTO program) {
//        programSelectorController.updateOptions(program);
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
