package gui.components.toolbar;

import dto.ProgramDTO;
import gui.components.expandationlevelwindow.ExpandationLevelWindowController;
import gui.components.instructionswindow.InstructionsWindowController;
import javafx.fxml.FXML;

public class InstructionsWindowToolbarController {
    private InstructionsWindowController instructionsWindowController;

    @FXML
    private ExpandationLevelWindowController expandationLevelWindowController;

    @FXML
    public void initialize() {
        expandationLevelWindowController.setInstructionsWindowToolbarController(this);
    }
    public void updateExpandationLevelWindow(int maximalDegree) {
//        int currentExpandationLevel = programDTO.getExpandLevelDegree();
//        int maximalExpandationLevel = programDTO.getMaximalDegree();
//        expandationLevelWindowController.setMaximalDegree(maximalDegree);
//        expandationLevelWindowController.currentDegreeField.setText(String.valueOf(currentExpandationLevel));

    }

    public void onDegreeChoice(int newDegree) {
        instructionsWindowController.onDegreeChoice(newDegree);
        System.out.println("New expandation level set to: " + newDegree);
        // Further actions to handle the change in expandation level can be implemented here
    }

    public void setInstructionsWindowController(InstructionsWindowController instructionsWindowToolbarController) {
        this.instructionsWindowController = instructionsWindowToolbarController;
    }

    public void onProgramLoaded(int maximalDegree) {
        expandationLevelWindowController.onProgramLoaded(maximalDegree);
    }

}
