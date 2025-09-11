package gui.components.instructionswindow;

import dto.InstructionDTO;
import dto.ProgramDTO;
import gui.components.instructionstable.InstructionsTableController;
import gui.components.summaryline.SummaryLineController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import logic.model.instruction.InstructionType;

import java.util.List;
import java.util.Map;

public class InstructionWindowController {
    @FXML
    private InstructionsTableController instructionsTableController;

    @FXML
    private InstructionsTableController instructionHistoryChainTableController;

    @FXML
    private SummaryLineController summaryLineController;

    @FXML
    public void initialize() {
        instructionsTableController.getTable().getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<InstructionDTO>() {
                    @Override
                    public void changed(ObservableValue<? extends InstructionDTO> obs, InstructionDTO oldSel, InstructionDTO newSel) {
                        if (newSel != null) {
                            List<InstructionDTO> instructionHistoryChain = newSel.getParentInstructions();
                            if (instructionHistoryChain != null) {
                                instructionHistoryChainTableController.setInstructions(instructionHistoryChain);
                            } else {
                                instructionHistoryChainTableController.setInstructions(List.of());
                            }
                        } else {
                            instructionHistoryChainTableController.setInstructions(List.of());
                        }
                    }
                });
    }


    public void setInstructionsTableData(ProgramDTO programDTO) {
        instructionsTableController.setInstructions(programDTO.getInstructionsDTO());
        Map<InstructionType, Integer> instructionsTypeCount = programDTO.getInstructionsTypeCount();
        summaryLineController.setSummaryLineValues(instructionsTypeCount.get(InstructionType.BASIC),
                instructionsTypeCount.get(InstructionType.SYNTHETIC));
    }
}
