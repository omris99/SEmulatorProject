package gui.components.instructionswindow;

import dto.InstructionDTO;
import dto.ProgramDTO;
import gui.app.AppController;
import gui.components.instructionstable.InstructionsTableController;
import gui.components.summaryline.SummaryLineController;
import gui.components.toolbar.InstructionsWindowToolbarController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import logic.model.instruction.InstructionType;

import java.util.List;
import java.util.Map;

public class InstructionsWindowController {
    private AppController appController;

    @FXML
    private InstructionsTableController instructionsTableController;

    @FXML
    private InstructionsWindowToolbarController instructionsWindowToolbarController;

    @FXML
    private InstructionsTableController instructionHistoryChainTableController;

    @FXML
    private SummaryLineController summaryLineController;

    @FXML
    public void initialize() {
        instructionsWindowToolbarController.setInstructionsWindowController(this);
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


    public void onProgramLoaded(ProgramDTO programDTO) {
        updateInstructionsTableAndSummaryLine(programDTO);
        instructionsWindowToolbarController.onProgramLoaded(programDTO);
    }

    public void onExpandationLevelChanged(ProgramDTO programDTO) {
        updateInstructionsTableAndSummaryLine(programDTO);
        instructionsWindowToolbarController.updateHighlightOptions(programDTO);
    }

    private void updateInstructionsTableAndSummaryLine(ProgramDTO programDTO) {
        instructionsTableController.setInstructions(programDTO.getInstructionsDTO());
        Map<InstructionType, Integer> instructionsTypeCount = programDTO.getInstructionsTypeCount();
        summaryLineController.setSummaryLineValues(instructionsTypeCount.get(InstructionType.BASIC),
                instructionsTypeCount.get(InstructionType.SYNTHETIC));
    }

    public void onDegreeChoice(int newDegree) {
        appController.expandProgram(newDegree);

    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void onHighlightSelectionChange(String selection) {
        instructionsTableController.highlightInstructionsWithSelection(selection);
//        appController.highLightInstructionsWithSelection(selection);
    }

    public int getDegreeChoice() {
        return instructionsWindowToolbarController.getDegreeChoice();
    }

}
