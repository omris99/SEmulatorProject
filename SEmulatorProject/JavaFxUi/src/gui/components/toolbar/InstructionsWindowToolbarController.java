package gui.components.toolbar;

import dto.ProgramDTO;
import gui.components.expandationlevelwindow.ExpandationLevelWindowController;
import javafx.fxml.FXML;

public class InstructionsWindowToolbarController {
    @FXML
    private ExpandationLevelWindowController expandationLevelWindowController;

    public void updateExpandationLevelWindow(ProgramDTO programDTO) {
        int currentExpandationLevel = programDTO.getExpandLevelDegree();
        int maximalExpandationLevel = programDTO.getMaximalDegree();
        expandationLevelWindowController.setMaximalDegree(maximalExpandationLevel);
//        expandationLevelWindowController.currentDegreeField.setText(String.valueOf(currentExpandationLevel));
    }
}
