package gui.components.debuggerwindow;

import clientserverdto.RunResultsDTO;
import gui.components.architectureselector.ArchitectureSelectorController;
import gui.components.executionstatewindow.ExecutionStateWindowController;
import gui.components.inputrow.InputRowController;
import gui.execution.ExecutionMode;
import gui.execution.ExecutionScreenController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import gui.components.debuggercommandsbar.debuggerCommandsBarController;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class DebuggerWindowController {
    private ExecutionScreenController executionScreenController;

    @FXML
    private debuggerCommandsBarController debuggerCommandsBarController;

    @FXML
    private VBox inputVariablesContainer;

    @FXML
    private ExecutionStateWindowController executionStateWindowController;

    @FXML
    private ArchitectureSelectorController architectureSelectorController;

    private final List<InputRowController> inputVariableRows = new ArrayList<>();

    @FXML
    private void initialize() {
        debuggerCommandsBarController.setDebuggerWindowController(this);
        architectureSelectorController.setDebuggerWindowController(this);
    }

    public void reset() {
        inputVariablesContainer.getChildren().clear();
        inputVariableRows.clear();
        debuggerCommandsBarController.disableAllButtons();
        executionStateWindowController.reset();
        debuggerCommandsBarController.disableNewRunButton(false);
        architectureSelectorController.reset();
    }

    public void prepareForNewRun(List<String> names) {
        inputVariablesContainer.getChildren().clear();
        inputVariableRows.clear();

        for (String name : names) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/gui/components/inputrow/InputRow.fxml")
                );
                Node rowNode = loader.load();
                InputRowController rowController = loader.getController();
                rowController.setName(name);
                rowController.setValue("0");

                inputVariablesContainer.getChildren().add(rowNode);
                inputVariableRows.add(rowController);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        debuggerCommandsBarController.enableExecutionButtons();
    }

    public void onNewRunClick() {
        executionScreenController.prepareDebuggerForNewRun();
    }

    public void setExecutionScreenController(ExecutionScreenController executionScreenController) {
        this.executionScreenController = executionScreenController;
    }

    public void onStartClick() {
        executionScreenController.startProgramExecution(getInputVariablesValues(), architectureSelectorController.getSelectedArchitecture());
    }

    private Map<String, String> getInputVariablesValues() {
        return inputVariableRows.stream()
                .collect(java.util.stream.Collectors.toMap(InputRowController::getName,
                        InputRowController::getValue));
    }

    public void updateRunResultsAndFinishExecutionModeIfNeeded(RunResultsDTO results, ExecutionMode mode) {
        executionStateWindowController.updateTableAndCycles(results);
        if (results.isFinished()) {
            finishExecutionMode(mode);
        }
    }

    public void setInputVariablesValues(Map<String, Long> inputs) {
        for (InputRowController row : inputVariableRows) {
            if (inputs.containsKey(row.getName())) {
                row.setValue(String.valueOf(inputs.get(row.getName())));
            }
        }
    }

    public void setProgramArchitecture(String architecture) {
        architectureSelectorController.setSelectedArchitecture(architecture);
    }

    public void onDebugButtonClick() {
        executionScreenController.startDebuggingSession(getInputVariablesValues(), architectureSelectorController.getSelectedArchitecture());
    }

    public void onStepOverClick() {
        executionScreenController.executeNextDebugStep();
    }

    public void onStepBackwardClick() {
        executionScreenController.executePreviousDebugStep();
    }

    public void onStopButtonClick() {
        executionScreenController.stopDebuggingSession();
        finishExecutionMode(ExecutionMode.DEBUG);
    }

    public void onResumeClick() {
        executionScreenController.resumeDebuggerExecution();
    }

    public void disableInputFields(boolean disable) {
        for (InputRowController row : inputVariableRows) {
            row.disableInputFields(disable);
        }
    }

    public void finishExecutionMode(ExecutionMode mode) {
        debuggerCommandsBarController.disableNewRunButton(false);
        if(mode == ExecutionMode.DEBUG){
            debuggerCommandsBarController.debugModeButtons(true);
        }else{
            debuggerCommandsBarController.executionModeButtons(true);
        }
        disableInputFields(false);
        executionStateWindowController.unmarkHighlightedLines();
    }

    public void startExecutionMode(ExecutionMode mode) {
        executionStateWindowController.reset();
        debuggerCommandsBarController.disableNewRunButton(true);
        if(mode == ExecutionMode.DEBUG){
            debuggerCommandsBarController.debugModeButtons(false);
        }else{
            debuggerCommandsBarController.executionModeButtons(false);
        }
        disableInputFields(true);
    }

    public void onArchitectureSelected(String architecture) {
        executionScreenController.highlightInstructionsByArchitecture(architecture);
    }

}
