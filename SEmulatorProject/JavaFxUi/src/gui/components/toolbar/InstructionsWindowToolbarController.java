package gui.components.toolbar;

import dto.ProgramDTO;
import gui.components.expandationlevelwindow.ExpandationLevelWindowController;
import gui.components.instructionswindow.InstructionsWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InstructionsWindowToolbarController {
    private InstructionsWindowController instructionsWindowController;

    @FXML
    private ExpandationLevelWindowController expandationLevelWindowController;

    @FXML
    private Button expandButton;

    @FXML
    public void initialize() {
        expandationLevelWindowController.setInstructionsWindowToolbarController(this);
    }

    public void onDegreeChoice(int newDegree) {
        instructionsWindowController.onDegreeChoice(newDegree);
    }

    public void setInstructionsWindowController(InstructionsWindowController instructionsWindowToolbarController) {
        this.instructionsWindowController = instructionsWindowToolbarController;
    }

    public void onProgramLoaded(int maximalDegree) {
        expandationLevelWindowController.onProgramLoaded(maximalDegree);
    }

    public void onExpandButtonClick() {
        if(expandationLevelWindowController.getMaximalDegree() > expandationLevelWindowController.getCurrentDegree()) {

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

}
