package gui.execution;

import clientserverdto.*;
import gui.app.ClientManager;
import gui.components.debuggerwindow.DebuggerWindowController;
import gui.components.instructionswindow.InstructionsWindowController;
import gui.components.userInfoBanner.UserInfoBannerController;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import serverengine.logic.json.GsonFactory;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gui.app.ClientManager.showErrorAlert;

public class ExecutionScreenController {
    @FXML
    private InstructionsWindowController instructionsWindowController;
    @FXML
    private DebuggerWindowController debuggerWindowController;

    @FXML
    private Button backToDashboardButton;

    @FXML
    private UserInfoBannerController userInfoBannerController;

    @FXML
    private ClientManager clientManager;

    @FXML
    public void initialize() {
        instructionsWindowController.setExecutionScreenController(this);
        debuggerWindowController.setExecutionScreenController(this);
    }

    public void setProgramToExecute(UploadedProgramDTO selectedProgram) {
        RequestBody formBody = new FormBody.Builder()
                .add("programName", selectedProgram.getName())
                .build();

        Request request = new Request.Builder()
                .url(ServerPaths.SET_PROGRAM_TO_EXECUTE)
                .post(formBody)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Error Setting Program",
                        "Failed to set program to execute on server",
                        e.getMessage()));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        instructionsWindowController.onProgramLoaded(selectedProgram.getProgram());
                        resetComponents();
                    });
                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to set program to execute on server"),
                            null));
                }

                response.close();
            }
        });
    }

    private void resetComponents() {
        debuggerWindowController.reset();
//        historyWindowController.reset();
//        displayCommandsBarController.disableTreeTableViewAndSpecificExpansionButton(false);
    }

    @FXML
    public void onBackToDashboardButtonClicked(ActionEvent e) {
        clientManager.switchToDashBoard();
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
                    Platform.runLater(() -> instructionsWindowController.onExpandationLevelChanged(programDTO));
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

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public void startProgramExecution(Map<String, String> inputVariables) {
        int runDegree = instructionsWindowController.getDegreeChoice();
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

                    clientManager.updateUserInfo();
                }
                else {
                    ErrorAlertDTO error = GsonFactory.getGson().fromJson(responseBodyString, ErrorAlertDTO.class);
                    Platform.runLater(() -> showErrorAlert(error.getTitle(), error.getHeader(), error.getContent()));
                }

                response.close();
            }
        });
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

    public void startDebuggingSession(Map<String, String> inputVariables) {
        int runDegree = instructionsWindowController.getDegreeChoice();
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
                        instructionsWindowController.disableDegreeChoiceControls(true);
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
                    Platform.runLater(() -> instructionsWindowController.highlightNextInstructionToExecute(nextInstruction));
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



    private void finishExecutionMode() {
        debuggerWindowController.finishExecutionMode();
        instructionsWindowController.stopHighlightingNextInstructionToExecute();
        instructionsWindowController.disableDegreeChoiceControls(false);
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

                    clientManager.updateUserInfo();
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

                    clientManager.updateUserInfo();
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

    public void setActive() {
    }

    public void setUserInfo(UserDTO userDTO) {
        userInfoBannerController.updateUserInfo(userDTO);
    }

}
