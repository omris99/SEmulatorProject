package gui.components.instructionswindow;

import dto.InstructionDTO;
import dto.ProgramDTO;
import gui.app.AppController;
import gui.components.instructionstable.InstructionsTableController;
import gui.components.instructionstablewithbreakpoints.InstructionsTableWithBreakpointsController;
import gui.components.summaryline.SummaryLineController;
import gui.components.toolbar.InstructionsWindowToolbarController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import logic.model.functionsrepo.FunctionsRepo;
import logic.model.instruction.InstructionType;

import java.util.List;
import java.util.Map;

public class InstructionsWindowController {
    private AppController appController;

    @FXML
    private InstructionsTableWithBreakpointsController instructionsTableController;

    @FXML
    private InstructionsWindowToolbarController instructionsWindowToolbarController;

    @FXML
    private InstructionsTableController instructionHistoryChainTableController;

    @FXML
    private SummaryLineController summaryLineController;


    @FXML
    public void initialize() {
        instructionsWindowToolbarController.setInstructionsWindowController(this);
        instructionsTableController.setInstructionsWindowController(this);
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

    public void programChanged(ProgramDTO programDTO) {
        updateInstructionsTableAndSummaryLine(programDTO);
        instructionsWindowToolbarController.programChanged(programDTO);
    }

    public void onExpandationLevelChanged(ProgramDTO programDTO) {
        updateInstructionsTableAndSummaryLine(programDTO);
        instructionsWindowToolbarController.updateHighlightOptions(programDTO);
    }

    private void updateInstructionsTableAndSummaryLine(ProgramDTO programDTO) {
        instructionsTableController.setInstructions(programDTO.getInstructionsDTO());
        stopHighlightingNextInstructionToExecute();
        Map<InstructionType, Integer> instructionsTypeCount = programDTO.getInstructionsTypeCount();
        summaryLineController.setSummaryLineValues(instructionsTypeCount.get(InstructionType.BASIC),
                instructionsTypeCount.get(InstructionType.SYNTHETIC));
    }

    public void onDegreeChoice(int newDegree) {
        appController.showExpandedProgram(newDegree);

    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void onHighlightSelectionChange(String selection) {
        instructionsTableController.highlightInstructionsWithSelection(selection);
    }

    public void onProgramSelectorChange(String selection) {
        if(selection == null){
            return;
        }

        appController.changeLoadedProgramToFunction(selection);
    }


    public int getDegreeChoice() {
        return instructionsWindowToolbarController.getDegreeChoice();
    }

    public void highlightNextInstructionToExecute(InstructionDTO instruction){
        instructionsTableController.highlightNextInstructionToExecute(instruction.getIndex());
    }

    public void stopHighlightingNextInstructionToExecute(){
        instructionsTableController.stopHighlightingNextInstructionToExecute();
    }

    public void disableDegreeChoiceControls(boolean disable) {
        instructionsWindowToolbarController.disableDegreeChoiceControls(disable);
    }

    public void setProgramDegree(int degree){
        instructionsWindowToolbarController.setProgramDegree(degree);
    }

    public InstructionDTO onBreakpointToggled(int instructionIndex, boolean isSet){
        return appController.updateInstructionBreakpoint(instructionIndex, isSet);
    }

}
