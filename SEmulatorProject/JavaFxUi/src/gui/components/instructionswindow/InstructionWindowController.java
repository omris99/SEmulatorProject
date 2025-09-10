package gui.components.instructionswindow;

import dto.InstructionDTO;
import gui.components.instructionstable.InstructionsTableController;
import javafx.fxml.FXML;

import java.util.List;

public class InstructionWindowController {
    @FXML
    private InstructionsTableController instructionsTableController;

    public void setInstructionsTableData(List<InstructionDTO> instructions) {
        instructionsTableController.setInstructions(instructions);
    }
}
