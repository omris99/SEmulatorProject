package gui.app;

import dto.*;
import gui.components.debuggerwindow.DebuggerWindowController;
import gui.components.displaycommandsbar.DisplayCommandsBarController;
import gui.components.historywindow.HistoryWindowController;
import gui.components.instructionstreetable.InstructionsTreeTableController;
import gui.components.instructionswindow.InstructionsWindowController;
import gui.components.loadfilebar.LoadFileBarController;
import http.ServerPaths;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import logic.instructiontree.InstructionsTree;
import logic.json.GsonFactory;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO:
// 1. BUG in reRunSelectedHistory(RunResultsDTO selectedRun):
// The call to prepareDebuggerForNewRun() is asynchronous.
// As a result, the following lines are executed before the debugger is actually ready,
// so input variables are always reset to 0 instead of being restored correctly.

public class ClientController {
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

    public void loadProgramWithProgress(File selectedFile) {
        loadFileBarController.removeProgressBarErrorStyle();
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(15, 100);
                updateMessage(selectedFile.getAbsolutePath());
                Request request = HttpClientUtil.buildUploadFileRequest(selectedFile);
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

    private ProgramDTO fetchLoadedProgram() {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_LOADED_PROGRAM);

        try (Response response = HttpClientUtil.runSync(request)) {
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                Platform.runLater(() -> {
                    showErrorAlert("HTTP " + response.code() + " Error", "Failed to fetch ProgramDTO from server", null);
                });
                return null;
            }

            return GsonFactory.getGson().fromJson(responseBody, ProgramDTO.class);

        } catch (Exception e) {
            Platform.runLater(() -> {
                showErrorAlert("Error Fetching Program", "Failed to fetch ProgramDTO from server", e.getMessage());
            });

            return null;
        }
    }

    public void prepareDebuggerForNewRun() {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_INPUTS_NAMES);

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Error Fetching inputs names",
                        "Failed to fetch inputs names from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    String[] inputsNames = GsonFactory.getGson().fromJson(responseBodyString, String[].class);
                    Platform.runLater(() -> {
                        debuggerWindowController.prepareForNewRun(List.of(inputsNames));
                    });
                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to fetch input names from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void showExpandedProgram(int degree) {
        Request request = new Request.Builder()
                .url(HttpUrl.parse(ServerPaths.GET_EXPANDED_PROGRAM)
                        .newBuilder()
                        .addQueryParameter("degree", String.valueOf(degree))
                        .build()
                        .toString())
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Error Fetching Expanded Program",
                        "Failed to fetch expanded program from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    ProgramDTO programDTO = GsonFactory.getGson().fromJson(responseBodyString, ProgramDTO.class);
                    Platform.runLater(() -> instructionWindowController.onExpandationLevelChanged(programDTO));
                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to fetch expanded program from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void startProgramExecution(Map<String, String> inputVariables) {
        int runDegree = instructionWindowController.getDegreeChoice();
        Map<String, Object> payload = new HashMap<>();
        payload.put("runDegree", runDegree);
        payload.put("inputVariables", inputVariables);

        String json = GsonFactory.getGson().toJson(payload);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(ServerPaths.RUN_PROGRAM)
                .post(body)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Run Program failed",
                        "Failed to get program's run results from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    RunResultsDTO runResultsDTO = GsonFactory.getGson().fromJson(responseBodyString, RunResultsDTO.class);
                    Platform.runLater(() -> {
                        debuggerWindowController.updateRunResults(runResultsDTO);
                        finishExecutionMode();
                    });
                }
                else {
                    ErrorAlertDTO error = GsonFactory.getGson().fromJson(responseBodyString, ErrorAlertDTO.class);
                    Platform.runLater(() -> showErrorAlert(error.getTitle(), error.getHeader(), error.getContent()));
                }

                response.close();
            }
        });
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
        int runDegree = instructionWindowController.getDegreeChoice();
        Map<String, Object> payload = new HashMap<>();
        payload.put("runDegree", runDegree);
        payload.put("inputVariables", inputVariables);

        String json = GsonFactory.getGson().toJson(payload);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(ServerPaths.INIT_DEBUGGING_SESSION)
                .post(body)
                .build();
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Init Debugging Session failed",
                        "Failed to initialize debugging session from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    RunResultsDTO initialState = GsonFactory.getGson().fromJson(responseBodyString, RunResultsDTO.class);
                    Platform.runLater(() -> {
                        debuggerWindowController.startExecutionMode();
                        debuggerWindowController.updateRunResults(initialState);
                        fetchAndHighlightNextInstructionToExecute();
                        instructionWindowController.disableDegreeChoiceControls(true);
                        historyWindowController.disableReRunButton(true);
                        loadFileBarController.disableLoadButton(true);
                    });
                }
                else {
                    ErrorAlertDTO error = GsonFactory.getGson().fromJson(responseBodyString, ErrorAlertDTO.class);
                    Platform.runLater(() -> showErrorAlert(error.getTitle(), error.getHeader(), error.getContent()));
                }

                response.close();
            }
        });
    }

    public void executeNextDebugStep() {
        Request request = HttpClientUtil.createEmptyBodyPostRequest(ServerPaths.STEP_OVER);

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Step Over Error",
                        "Failed to Step Over in debugging session from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    RunResultsDTO context = GsonFactory.getGson().fromJson(responseBodyString, RunResultsDTO.class);
                    Platform.runLater(() -> debuggerWindowController.updateRunResults(context));
                    if (context.isFinished()) {
                        Platform.runLater(() -> finishExecutionMode());
                    } else {
                        fetchAndHighlightNextInstructionToExecute();
                    }
                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to Step Over in debugging session from server"),
                            null));
                }

                response.close();
            }
        });
    }

    private void fetchAndHighlightNextInstructionToExecute() {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_NEXT_INSTRUCTION_TO_EXECUTE);

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Error Fetching Next Instruction",
                        "Failed to fetch next instruction to execute from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    InstructionDTO nextInstruction = GsonFactory.getGson().fromJson(responseBodyString, InstructionDTO.class);
                    Platform.runLater(() -> instructionWindowController.highlightNextInstructionToExecute(nextInstruction));
                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to fetch next instruction to execute from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void executePreviousDebugStep() {
        Request request = HttpClientUtil.createEmptyBodyPostRequest(ServerPaths.STEP_BACKWARD);
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Step Backward Error",
                        "Failed to Step Backward in debugging session from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    RunResultsDTO context = GsonFactory.getGson().fromJson(responseBodyString, RunResultsDTO.class);
                    Platform.runLater(() -> {
                        debuggerWindowController.updateRunResults(context);
                        fetchAndHighlightNextInstructionToExecute();
                    });

                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to Step Backward in debugging session from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void stopDebuggingSession() {
        Request request = HttpClientUtil.createEmptyBodyPostRequest(ServerPaths.STOP_DEBUGGER_EXECUTION);
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Error Stopping Debugging Session",
                        "Failed to stop debugging session from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> finishExecutionMode());
                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to stop debugging session from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void resumeDebuggerExecution() {
        Request request = HttpClientUtil.createEmptyBodyPostRequest(ServerPaths.RESUME_DEBUGGER_EXECUTION);

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Error Resuming Debugger Execution",
                        "Failed to resume debugger execution from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    RunResultsDTO context = GsonFactory.getGson().fromJson(responseBodyString, RunResultsDTO.class);
                    Platform.runLater(() -> debuggerWindowController.updateRunResults(context));
                    if (context.isFinished()) {
                        Platform.runLater(() -> finishExecutionMode());
                    } else {
                        fetchAndHighlightNextInstructionToExecute();
                    }
                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to resume debugger execution from server"),
                            null));
                }

                response.close();
            }
        });
    }

    private void finishExecutionMode() {
        fetchHistoryAndUpdateHistoryWindow();
        debuggerWindowController.finishExecutionMode();
        instructionWindowController.stopHighlightingNextInstructionToExecute();
        instructionWindowController.disableDegreeChoiceControls(false);
        historyWindowController.disableReRunButton(false);
        loadFileBarController.disableLoadButton(false);
    }

    public void changeLoadedProgramToFunction(String functionName) {
        RequestBody formBody = new FormBody.Builder()
                .add("functionName", functionName)
                .build();

        Request request = new Request.Builder()
                .url(ServerPaths.CHANGE_ON_SCREEN_PROGRAM)
                .post(formBody)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Error Changing On-Screen Program",
                        "Failed to change on-screen program from server",
                        e.getMessage()));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    ProgramDTO programDTO = GsonFactory.getGson().fromJson(responseBodyString, ProgramDTO.class);
                    Platform.runLater(() -> {
                        instructionWindowController.programChanged(programDTO);
                        resetComponents();
                        fetchHistoryAndUpdateHistoryWindow();
                    });
                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to change on-screen program from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void disableAnimations(boolean enable) {
        AnimationsManager.setAnimationsDisabled(enable);
    }

    public void changeTheme(Theme theme) {
        Main.applyTheme(theme);
    }

    public InstructionDTO updateInstructionBreakpoint(int index, boolean isSet) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("index", index);
        payload.put("isSet", isSet);

        String json = GsonFactory.getGson().toJson(payload);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(ServerPaths.UPDATE_INSTRUCTION_BREAKPOINT)
                .post(body)
                .build();

        try (Response response = HttpClientUtil.runSync(request)) {
            String responseBodyString = response.body().string();
            if (response.isSuccessful()) {
                return GsonFactory.getGson().fromJson(responseBodyString, InstructionDTO.class);
            } else {
                Platform.runLater(() -> showErrorAlert(
                        ("HTTP " + response.code() + " Error"),
                        ("Failed to update instruction breakpoint on server"),
                        responseBodyString));
            }
        } catch (Exception e) {
            Platform.runLater(() -> showErrorAlert(
                    "Error Updating Instruction Breakpoint",
                    "Failed to update instruction breakpoint on server",
                    e.getMessage()));
        }

        return null;
    }

    public void showOnScreenProgramTreeTableView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/components/instructionstreetable/InstructionsTreeTable.fxml"));
            Parent load = loader.load();
            InstructionsTreeTableController controller = loader.getController();

            Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_ON_SCREEN_PROGRAM_INSTRUCTIONS_TREE);
            HttpClientUtil.runAsync(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Platform.runLater(() -> showErrorAlert(
                            "Error Fetching Instructions Tree",
                            "Failed to fetch instructions tree from server",
                            e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBodyString = response.body().string();
                    if (response.isSuccessful()) {
                        InstructionsTree instructionsTree = GsonFactory.getGson().fromJson(responseBodyString, InstructionsTree.class);
                        Platform.runLater(() -> {
                            controller.setInstructions(instructionsTree);
                            Scene scene = new Scene(load, 700, 400);
                            Stage showWindow = new Stage();
                            showWindow.setTitle("Tree Table View");
                            showWindow.setScene(scene);
                            showWindow.show();
                        });
                    } else {
                        Platform.runLater(() -> showErrorAlert(
                                ("HTTP " + response.code() + " Error"),
                                ("Failed to fetch instructions tree from server"),
                                null));
                    }

                    response.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSpecificExpansionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/components/instructionstreetable/InstructionsTreeTable.fxml"));
            Parent load = loader.load();
            InstructionsTreeTableController controller = loader.getController();

            Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_SPECIFIC_EXPANSION_INSTRUCTIONS_TREE);
            HttpClientUtil.runAsync(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Platform.runLater(() -> showErrorAlert(
                            "Error Fetching Specific Expansion Instructions Tree",
                            "Failed to fetch specific expansion instructions tree from server",
                            e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBodyString = response.body().string();
                    if (response.isSuccessful()) {
                        InstructionsTree instructionsTree = GsonFactory.getGson().fromJson(responseBodyString, InstructionsTree.class);
                        Platform.runLater(() -> {
                            controller.setInstructions(instructionsTree);
                            Scene scene = new Scene(load, 700, 400);
                            Stage showWindow = new Stage();
                            showWindow.setTitle("Specific Expansion View");
                            showWindow.setScene(scene);
                            showWindow.show();
                        });
                    } else {
                        Platform.runLater(() -> showErrorAlert(
                                ("HTTP " + response.code() + " Error"),
                                ("Failed to fetch specific expansion instructions tree from server"),
                                null));
                    }

                    response.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchHistoryAndUpdateHistoryWindow() {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_HISTORY);
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Error Fetching History",
                        "Failed to fetch history from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    RunResultsDTO[] historyArray = GsonFactory.getGson().fromJson(responseBodyString, RunResultsDTO[].class);
                    Platform.runLater(() -> updateHistoryWindow(List.of(historyArray)));
                } else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to fetch history from server"),
                            null));
                }

                response.close();
            }
        });
    }
}
