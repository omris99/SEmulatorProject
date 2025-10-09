package gui.components.instructionswindow;

import clientserverdto.InstructionDTO;
import clientserverdto.ProgramDTO;
import gui.components.instructionstable.InstructionsTableController;
import gui.components.instructionstablewithbreakpoints.InstructionsTableWithBreakpointsController;
import gui.components.summaryline.SummaryLineController;
import gui.components.toolbar.InstructionsWindowToolbarController;
import gui.execution.ExecutionScreenController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import types.ArchitectureType;
import types.InstructionType;

import java.util.List;
import java.util.Map;

public class InstructionsWindowController {

    @FXML
    private ExecutionScreenController executionScreenController;

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
        Map<ArchitectureType, Integer> instructionsCountByArchitecture = programDTO.getInstructionsCountByArchitecture();
        summaryLineController.setSummaryLineValues(
                instructionsTypeCount,
                instructionsCountByArchitecture
                );
    }

    public void onDegreeChoice(int newDegree) {
        executionScreenController.showExpandedProgram(newDegree);
    }

    public void onHighlightSelectionChange(String selection) {
        instructionsTableController.highlightInstructionsWithSelection(selection);
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
        return executionScreenController.updateInstructionBreakpoint(instructionIndex, isSet);
    }

    public void setExecutionScreenController(ExecutionScreenController executionScreenController) {
        this.executionScreenController = executionScreenController;
    }

    public void highlightInstructionsByArchitecture(String architecture){
        instructionsTableController.highlightInstructionsByArchitecture(architecture);
    }
}
