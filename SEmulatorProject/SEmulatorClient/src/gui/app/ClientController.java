package gui.app;

import com.google.gson.JsonObject;
import dto.DTO;
import dto.InstructionDTO;
import dto.ProgramDTO;
import dto.RunResultsDTO;
import gui.components.debuggerwindow.DebuggerWindowController;
import gui.components.displaycommandsbar.DisplayCommandsBarController;
import gui.components.historywindow.HistoryWindowController;
import gui.components.instructionstreetable.InstructionsTreeTableController;
import gui.components.instructionswindow.InstructionsWindowController;
import gui.components.loadfilebar.LoadFileBarController;
import http.Constants;
import http.HttpClientUtil;
import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import logic.engine.EmulatorEngine;
import logic.exceptions.InvalidArgumentException;
import logic.exceptions.InvalidXmlFileException;
import logic.exceptions.NumberNotInRangeException;
import logic.instructiontree.InstructionsTree;
import logic.json.GsonFactory;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClientController {
    private final EmulatorEngine engine;

    @FXML
    private LoadFileBarController loadFileBarController;

    @FXML
    private InstructionsWindowController instructionWindowController;

    @FXML
    private HistoryWindowController historyWindowController;

    @FXML
    private DebuggerWindowController debuggerWindowController;

    @FXML
    private DisplayCommandsBarController displayCommandsBarController;

    @FXML
    private void initialize() {
        loadFileBarController.setClientController(this);
        debuggerWindowController.setClientController(this);
        instructionWindowController.setClientController(this);
        historyWindowController.setClientController(this);
        displayCommandsBarController.setCLientController(this);
    }

    public ClientController() {
        this.engine = new EmulatorEngine();
    }

    public void loadProgramWithProgress(File selectedFile) {
        loadFileBarController.removeProgressBarErrorStyle();
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(15, 100);
                updateMessage(selectedFile.getAbsolutePath());
                Request request = HttpClientUtil.buildUploadRequest(selectedFile);
                HttpClientUtil.runAsync(request, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Platform.runLater(() -> {
                            loadFileBarController.setProgressBarLoadErrorStyle();
                            showErrorAlert(
                                    "Upload Failed",
                                    "Failed to upload file to server",
                                    e.getMessage()
                            );
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBodyString = response.body().string();
                        if (response.isSuccessful()) {
                            ProgramDTO programDTO = GsonFactory.getGson().fromJson(responseBodyString, ProgramDTO.class);
                            Platform.runLater(() -> {
                                instructionWindowController.onProgramLoaded(programDTO);
                                resetComponents();
                            });
                        } else {
                            Platform.runLater(() -> {
                                loadFileBarController.setProgressBarLoadErrorStyle();
                                showErrorAlert(
                                        ("HTTP " + response.code() + " Error"),
                                        ("Failed to load XML file"),
                                        responseBodyString);
                            });
                        }
                        response.close();
                    }
                });

                updateProgress(100, 100);
                return null;
            }
        };

        loadFileBarController.bindTaskToUI(loadTask);

        new Thread(loadTask).start();
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void prepareDebuggerForNewRun() {
        debuggerWindowController.prepareForNewRun(((ProgramDTO) engine.getLoadedProgramDTO()).getInputNames());
    }

    public void showExpandedProgram(int degree) {
        instructionWindowController.onExpandationLevelChanged((ProgramDTO) engine.showExpandedProgramOnScreen(degree));
    }

    public void startProgramExecution(Map<String, String> inputVariables) {
        try {
            int runDegree = instructionWindowController.getDegreeChoice();
            DTO runResultsDTO = engine.runLoadedProgramWithDebuggerWindowInput(runDegree, inputVariables);
            debuggerWindowController.updateRunResults((RunResultsDTO) runResultsDTO);
            finishExecutionMode();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Starting Execution");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("The input is invalid. Please enter integers only.");
            alert.showAndWait();
        } catch (NumberNotInRangeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Starting Execution");
            alert.setHeaderText("Negative Number Submitted");
            alert.setContentText("You entered the number: " + e.getNumber() + " which is not positive.\n" +
                    "Please enter only Positive Numbers.");
            alert.showAndWait();
        }
    }

    private void updateHistoryWindow(List<RunResultsDTO> history) {
        historyWindowController.updateHistoryTable(history);
    }

    public void reRunSelectedHistory(RunResultsDTO selectedRun) {
        prepareDebuggerForNewRun();
        instructionWindowController.setProgramDegree(selectedRun.getDegree());
        debuggerWindowController.setInputVariablesValues(selectedRun.getInputVariablesInitialValues());
    }

    private void resetComponents() {
        debuggerWindowController.reset();
        historyWindowController.reset();
        displayCommandsBarController.disableTreeTableViewAndSpecificExpansionButton(false);
    }

    public void startDebuggingSession(Map<String, String> inputVariables) {
        try {
            int runDegree = instructionWindowController.getDegreeChoice();
            DTO initialState = engine.initDebuggingSession(runDegree, inputVariables);
            debuggerWindowController.startExecutionMode();
            debuggerWindowController.updateRunResults((RunResultsDTO) initialState);
            instructionWindowController.highlightNextInstructionToExecute((InstructionDTO) engine.getNextInstructionToExecute());
            instructionWindowController.disableDegreeChoiceControls(true);
            historyWindowController.disableReRunButton(true);
            loadFileBarController.disableLoadButton(true);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Starting Execution");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("The input is invalid. Please enter integers only.");
            alert.showAndWait();
        } catch (NumberNotInRangeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Starting Execution");
            alert.setHeaderText("Negative Number Submitted");
            alert.setContentText("You entered the number: " + e.getNumber() + " which is not positive.\n" +
                    "Please enter only Positive Numbers.");
            alert.showAndWait();
        }
    }

    public void executeNextDebugStep() {
        RunResultsDTO context = (RunResultsDTO) engine.stepOver();
        debuggerWindowController.updateRunResults(context);
        if (context.isFinished()) {
            finishExecutionMode();
        } else {
            instructionWindowController.highlightNextInstructionToExecute((InstructionDTO) engine.getNextInstructionToExecute());
        }
    }

    public void executePreviousDebugStep() {
        RunResultsDTO context = (RunResultsDTO) engine.stepBackward();
        debuggerWindowController.updateRunResults(context);
        instructionWindowController.highlightNextInstructionToExecute((InstructionDTO) engine.getNextInstructionToExecute());
    }

    public void stopDebuggingSession() {
        engine.stopDebuggingSession();
        finishExecutionMode();
    }

    public void resumeDebuggerExecution() {
        DTO context = engine.resumeDebuggingSession();
        debuggerWindowController.updateRunResults((RunResultsDTO) context);
        if (((RunResultsDTO) context).isFinished()) {
            finishExecutionMode();
        } else {
            instructionWindowController.highlightNextInstructionToExecute((InstructionDTO) engine.getNextInstructionToExecute());
        }
    }

    private void finishExecutionMode() {
        updateHistoryWindow(engine.getHistory());
        debuggerWindowController.finishExecutionMode();
        instructionWindowController.stopHighlightingNextInstructionToExecute();
        instructionWindowController.disableDegreeChoiceControls(false);
        historyWindowController.disableReRunButton(false);
        loadFileBarController.disableLoadButton(false);
    }

    public void changeLoadedProgramToFunction(String functionName) {
        engine.changeLoadedProgramToFunction(functionName);
        ProgramDTO programDTO = (ProgramDTO) engine.getLoadedProgramDTO();
        instructionWindowController.programChanged(programDTO);
        resetComponents();
        updateHistoryWindow(engine.getHistory());
    }

    public void disableAnimations(boolean enable) {
        AnimationsManager.setAnimationsDisabled(enable);
    }

    public void changeTheme(Theme theme) {
        Main.applyTheme(theme);
    }

    public InstructionDTO updateInstructionBreakpoint(int index, boolean isSet) {
        return engine.updateInstructionBreakpoint(index, isSet);
    }

    public void showOnScreenProgramTreeTableView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/components/instructionstreetable/InstructionsTreeTable.fxml"));
            Parent load = loader.load();
            InstructionsTreeTableController controller = loader.getController();
            InstructionsTree instructionsTree = engine.getOnScreenProgramInstructionsTree();
            controller.setInstructions(instructionsTree);
            Scene scene = new Scene(load, 700, 400);
            Stage showWindow = new Stage();
            showWindow.setTitle("Tree Table View");
            showWindow.setScene(scene);
            showWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSpecificExpansionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/components/instructionstreetable/InstructionsTreeTable.fxml"));
            Parent load = loader.load();
            InstructionsTreeTableController controller = loader.getController();
            InstructionsTree instructionsTree = engine.getSpecificExpansionInstructionsTree();
            controller.setInstructions(instructionsTree);
            Scene scene = new Scene(load, 700, 400);
            Stage showWindow = new Stage();
            showWindow.setTitle("Specific Expansion View");
            showWindow.setScene(scene);
            showWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
