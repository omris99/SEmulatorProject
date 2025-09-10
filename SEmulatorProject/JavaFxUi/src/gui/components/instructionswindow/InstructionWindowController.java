package gui.components.instructionswindow;

import dto.InstructionDTO;
import dto.ProgramDTO;
import gui.components.instructionstable.InstructionsTableController;
import gui.components.summaryline.SummaryLineController;
import javafx.fxml.FXML;
import logic.model.instruction.InstructionType;

import java.util.List;
import java.util.Map;

public class InstructionWindowController {
    @FXML
    private InstructionsTableController instructionsTableController;

    @FXML
    private SummaryLineController summaryLineController;

    public void setInstructionsTableData(ProgramDTO programDTO) {
        instructionsTableController.setInstructions(programDTO.getInstructionsDTO());
        Map<InstructionType, Integer> instructionsTypeCount = programDTO.getInstructionsTypeCount();
        summaryLineController.setSummaryLineValues(instructionsTypeCount.get(InstructionType.BASIC),
                instructionsTypeCount.get(InstructionType.SYNTHETIC));
    }
}
