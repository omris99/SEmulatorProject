package gui.app;

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
import gui.popups.showallvariables.ShowAllVariablesController;
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

import java.io.File;
import java.util.List;
import java.util.Map;

public class AppController {
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
        loadFileBarController.setAppController(this);
        debuggerWindowController.setAppController(this);
        instructionWindowController.setAppController(this);
        historyWindowController.setAppController(this);
        displayCommandsBarController.setAppController(this);
    }

    public AppController() {
        this.engine = new EmulatorEngine();
    }

    public void loadProgramWithProgress(File selectedFile) {
        loadFileBarController.removeProgressBarErrorStyle();
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    updateProgress(15, 100);
//                    updateMessage("Loading program...");
                    updateMessage(selectedFile.getAbsolutePath());
                    engine.loadProgram(selectedFile.getAbsolutePath());
                    updateProgress(50, 100);
                    Thread.sleep(100);

                    ProgramDTO programDTO = (ProgramDTO) engine.getLoadedProgramDTO();
                    Platform.runLater(() -> {
                        instructionWindowController.onProgramLoaded(programDTO);
                        resetComponents();
                    });

                    updateProgress(100, 100);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Loading Program");
                        alert.setHeaderText("Failed to load XML file");

                        String content;
                        switch (e) {
                            case InvalidXmlFileException iv -> {
                                switch (iv.getType()) {
                                    case FILE_MISSING -> content = "File not found: " + iv.getFilePath();
                                    case INVALID_EXTENSION ->
                                            content = "Invalid file type: " + iv.getFilePath() + " must be .xml";
                                    case UNKNOWN_LABEL ->
                                            content = String.format("No instruction labeled as %s exists in the program.", iv.getElement());
                                    default -> content = "Unknown InvalidXmlFileException";
                                }
                            }
                            case JAXBException jaxb -> content = "Can't read XML File";
                            case InvalidArgumentException ia -> content = String.format("%s.  \nError %s: %s ",
                                    ia.getErrorType().getUserMessage(),
                                    ia.getErrorType().getArgumentType(),
                                    ia.getArgumentName());
                            case IllegalArgumentException iae -> content = "Invalid XML File: " + iae.getMessage();
                            default -> content = "Unexpected error: " + e.getMessage();
                        }

                        alert.setContentText(content);
                        alert.showAndWait();
                    });

                    if (!engine.isProgramLoaded()) {
                        updateMessage(String.format("Failed to load File: %s", selectedFile.getName()));
                    }
                    loadFileBarController.setProgressBarLoadErrorStyle();
                    updateProgress(100, 100);
                }

                return null;
            }
        };

        loadFileBarController.bindTaskToUI(loadTask);

        new Thread(loadTask).start();
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
        displayCommandsBarController.disableTreeTableViewButton(false);
    }

    public void startDebuggingSession(Map<String, String> inputVariables) {
        try {
            int runDegree = instructionWindowController.getDegreeChoice();
            DTO initialState = engine.initDebuggingSession(runDegree, inputVariables);
            debuggerWindowController.startExecutionMode();
            debuggerWindowController.updateRunResults((RunResultsDTO) initialState);
            instructionWindowController.highlightNextInstructionToExecute((InstructionDTO) engine.getNextInstructionToExecute());
            instructionWindowController.disableDegreeChoiceControls(true);
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
        }
        else {
            instructionWindowController.highlightNextInstructionToExecute((InstructionDTO) engine.getNextInstructionToExecute());
        }
    }

    public void executePreviousDebugStep(){
        RunResultsDTO context = (RunResultsDTO) engine.stepBackward();
        debuggerWindowController.updateRunResults(context);
        instructionWindowController.highlightNextInstructionToExecute((InstructionDTO) engine.getNextInstructionToExecute());
    }

    public void stopDebuggingSession() {
        engine.stopDebuggingSession();
//        instructionWindowController.stopHighlightingNextInstructionToExecute();
        finishExecutionMode();
    }

    public void resumeDebuggerExecution() {
        DTO context = engine.resumeDebuggingSession();
        debuggerWindowController.updateRunResults((RunResultsDTO) context);
        if(((RunResultsDTO) context).isFinished()){
            finishExecutionMode();
        }
        else {
            instructionWindowController.highlightNextInstructionToExecute((InstructionDTO) engine.getNextInstructionToExecute());
        }
//        instructionWindowController.stopHighlightingNextInstructionToExecute();
//        updateHistoryWindow(engine.getHistory());
//        finishExecutionMode();
    }

    private void finishExecutionMode() {
        updateHistoryWindow(engine.getHistory());
        debuggerWindowController.finishExecutionMode();
        instructionWindowController.stopHighlightingNextInstructionToExecute();
        instructionWindowController.disableDegreeChoiceControls(false);
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

    public void showTreeTableView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/components/instructionstreetable/InstructionsTreeTable.fxml"));
            Parent load = loader.load();
            InstructionsTreeTableController controller = loader.getController();
            InstructionsTree instructionsTree = engine.getInstructionsTree();
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
}
