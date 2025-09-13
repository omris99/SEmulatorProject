package gui.components.debuggerwindow;

import dto.RunResultsDTO;
import gui.app.AppController;
import gui.components.executionstatewindow.ExecutionStateWindowController;
import gui.components.historywindow.HistoryWindowController;
import gui.components.inputrow.InputRowController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import gui.components.debuggercommandsbar.debuggerCommandsBarController;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DebuggerWindowController {
    private AppController appController;

    @FXML
    private debuggerCommandsBarController debuggerCommandsBarController;

    @FXML
    private VBox inputVariablesContainer;

    @FXML
    private ExecutionStateWindowController executionStateWindowController;

    private final List<InputRowController> inputVariableRows = new ArrayList<>();

    @FXML
    private void initialize() {
        debuggerCommandsBarController.setDebuggerWindowController(this);
    }

    public void reset() {
        inputVariablesContainer.getChildren().clear();
        inputVariableRows.clear();
        debuggerCommandsBarController.reset();
        executionStateWindowController.reset();
        debuggerCommandsBarController.enableNewRunButton();
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


    public void clearInputVariablesTable() {
        inputVariableRows.clear();
    }

    public void onNewRunClick() {
        appController.prepareDebuggerForNewRun();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void onStartClick() {
        appController.startProgramExecution(getInputVariablesValues());
    }

    private Map<String, String> getInputVariablesValues() {
        return inputVariableRows.stream()
                .collect(java.util.stream.Collectors.toMap(InputRowController::getName,
                        InputRowController::getValue));
    }

    public void updateRunResults(RunResultsDTO results) {
        executionStateWindowController.updateTableAndCycles(results);
    }

    public void setInputVariablesValues(Map<String, Long> inputs){
        for(InputRowController row : inputVariableRows){
            if(inputs.containsKey(row.getName())){
                row.setValue(String.valueOf(inputs.get(row.getName())));
            }
        }
    }
}
