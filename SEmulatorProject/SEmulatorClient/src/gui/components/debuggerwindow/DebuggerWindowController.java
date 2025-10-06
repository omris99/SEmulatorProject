package gui.components.debuggerwindow;

import clientserverdto.ProgramDTO;
import clientserverdto.RunResultsDTO;
import gui.components.architectureselector.ArchitectureSelectorController;
import gui.components.executionstatewindow.ExecutionStateWindowController;
import gui.components.inputrow.InputRowController;
import gui.execution.ExecutionScreenController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import gui.components.debuggercommandsbar.debuggerCommandsBarController;
import javafx.scene.layout.VBox;
import serverengine.logic.model.instruction.ArchitectureType;

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
        debuggerCommandsBarController.reset();
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
        executionScreenController.startProgramExecution(getInputVariablesValues());
    }

    private Map<String, String> getInputVariablesValues() {
        return inputVariableRows.stream()
                .collect(java.util.stream.Collectors.toMap(InputRowController::getName,
                        InputRowController::getValue));
    }

    public void updateRunResults(RunResultsDTO results) {
        executionStateWindowController.updateTableAndCycles(results);
        if (results.isFinished()) {
            finishExecutionMode();
        }
    }

    public void setInputVariablesValues(Map<String, Long> inputs) {
        for (InputRowController row : inputVariableRows) {
            if (inputs.containsKey(row.getName())) {
                row.setValue(String.valueOf(inputs.get(row.getName())));
            }
        }
    }

    public void onDebugButtonClick() {
        executionScreenController.startDebuggingSession(getInputVariablesValues());
    }

    public void onStepOverClick() {
        executionScreenController.executeNextDebugStep();
    }

    public void onStepBackwardClick() {
        executionScreenController.executePreviousDebugStep();
    }

    public void onStopButtonClick() {
        executionScreenController.stopDebuggingSession();
        finishExecutionMode();
    }

    public void onResumeClick() {
        executionScreenController.resumeDebuggerExecution();
    }

    public void disableInputFields(boolean disable) {
        for (InputRowController row : inputVariableRows) {
            row.disableInputFields(disable);
        }
    }

    public void finishExecutionMode() {
        debuggerCommandsBarController.disableNewRunButton(false);
        debuggerCommandsBarController.disableDebuggerControlButtons(true);
        disableInputFields(false);
        executionStateWindowController.unmarkHighlightedLines();
    }

    public void startExecutionMode() {
        executionStateWindowController.reset();
        debuggerCommandsBarController.disableNewRunButton(true);
        debuggerCommandsBarController.disableDebuggerControlButtons(false);
        disableInputFields(true);
    }

    public void onArchitectureSelected(String architecture) {
        executionScreenController.highlightInstructionsByArchitecture(architecture);
    }

}
