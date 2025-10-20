package gui.screens.execution;

import clientserverdto.*;
import clientserverdto.instructiontree.InstructionsTree;
import gui.components.dynamicexecutiondatawindow.DynamicExecutionDataWindowController;
import gui.components.treetablecommandsbar.TreeTableCommandsBarController;
import gui.components.instructionstreetable.InstructionsTreeTableController;
import gui.utils.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import types.errortypes.ExecutionErrorType;
import gui.app.ClientController;
import gui.components.debuggerwindow.DebuggerWindowController;
import gui.components.instructionswindow.InstructionsWindowController;
import gui.components.userInfoBanner.UserInfoBannerController;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import okhttp3.*;
import json.GsonFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ClientController clientController;

    @FXML
    private TreeTableCommandsBarController treeTableCommandsBarController;

    @FXML
    public void initialize() {
        instructionsWindowController.setExecutionScreenController(this);
        debuggerWindowController.setExecutionScreenController(this);
        treeTableCommandsBarController.setExecutionScreenController(this);
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
                Platform.runLater(() -> Utils.showErrorAlert(
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
                    Platform.runLater(() -> Utils.showErrorAlert(
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
        treeTableCommandsBarController.disableTreeTableViewAndSpecificExpansionButton(false);
    }

    @FXML
    public void onBackToDashboardButtonClicked(ActionEvent e) {
        clientController.switchToDashBoard();
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
                Platform.runLater(() -> Utils.showErrorAlert(
                        "Error Fetching Expanded Program",
                        "Failed to fetch expanded program from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    ProgramDTO programDTO = GsonFactory.getGson().fromJson(responseBodyString, ProgramDTO.class);
                    Platform.runLater(() -> {
                        instructionsWindowController.onExpandationLevelChanged(programDTO);
                    });
                }
                else {
                    Platform.runLater(() -> Utils.showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to fetch expanded program from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void setClientManager(ClientController clientController) {
        this.clientController = clientController;
    }

    public void startProgramExecution(Map<String, String> inputVariables, String architecture) {
        int runDegree = instructionsWindowController.getDegreeChoice();
        Map<String, Object> payload = new HashMap<>();
        payload.put("runDegree", runDegree);
        payload.put("inputVariables", inputVariables);
        payload.put("architecture", architecture);

        String json = GsonFactory.getGson().toJson(payload);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(ServerPaths.RUN_PROGRAM)
                .post(body)
                .build();

        startExecutionMode(ExecutionMode.REGULAR);

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    finishExecutionMode(ExecutionMode.REGULAR);
                    Utils.showErrorAlert(
                            "Run Program failed",
                            "Failed to get program's run results from server",
                            e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    activateDynamicExecutionDataWindow();
//                    RunResultsDTO runResultsDTO = GsonFactory.getGson().fromJson(responseBodyString, RunResultsDTO.class);
//                    Platform.runLater(() -> {
//                        debuggerWindowController.updateRunResultsAndFinishExecutionModeIfNeeded(runResultsDTO, ExecutionMode.REGULAR);
//                        finishExecutionMode(ExecutionMode.REGULAR);
//                    });
                }
                else {
                    ErrorDTO error = GsonFactory.getGson().fromJson(responseBodyString, ErrorDTO.class);
                    Platform.runLater(() -> handleError(error, ExecutionMode.REGULAR));
                }

                clientController.updateUserInfo();

                response.close();
            }
        });
    }

    public void handleError(ErrorDTO errorDTO, ExecutionMode executionMode) {
        Utils.showErrorAlert(errorDTO.getTitle(), errorDTO.getHeader(), errorDTO.getContent());
        finishExecutionMode(executionMode);
        if(errorDTO.getType() == ExecutionErrorType.CREDIT_BALANCE_TOO_LOW)
        {
            clientController.switchToDashBoard();
        }
    }

    public void prepareDebuggerForNewRun() {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_INPUTS_NAMES);

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> Utils.showErrorAlert(
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
                    Platform.runLater(() -> Utils.showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to fetch input names from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void startDebuggingSession(Map<String, String> inputVariables, String architecture) {
        int runDegree = instructionsWindowController.getDegreeChoice();
        Map<String, Object> payload = new HashMap<>();
        payload.put("runDegree", runDegree);
        payload.put("inputVariables", inputVariables);
        payload.put("architecture", architecture);

        String json = GsonFactory.getGson().toJson(payload);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(ServerPaths.INIT_DEBUGGING_SESSION)
                .post(body)
                .build();
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> Utils.showErrorAlert(
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
                        startExecutionMode(ExecutionMode.DEBUG);
                        debuggerWindowController.updateRunResultsAndFinishExecutionModeIfNeeded(initialState, ExecutionMode.DEBUG);
                    });

                }
                else {
                    ErrorDTO error = GsonFactory.getGson().fromJson(responseBodyString, ErrorDTO.class);
                    Platform.runLater(() -> handleError(error, ExecutionMode.DEBUG));
                }

                clientController.updateUserInfo();

                response.close();
            }
        });
    }

    private void fetchAndHighlightNextInstructionToExecute() {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_NEXT_INSTRUCTION_TO_EXECUTE);

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> Utils.showErrorAlert(
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
                    Platform.runLater(() -> Utils.showErrorAlert(
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
                Platform.runLater(() -> Utils.showErrorAlert(
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
                        debuggerWindowController.updateRunResultsAndFinishExecutionModeIfNeeded(context, ExecutionMode.DEBUG);
                        fetchAndHighlightNextInstructionToExecute();
                    });

                }
                else {
                    Platform.runLater(() -> Utils.showErrorAlert(
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
                Platform.runLater(() -> Utils.showErrorAlert(
                        "Error Stopping Debugging Session",
                        "Failed to stop debugging session from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> finishExecutionMode(ExecutionMode.DEBUG));
                }
                else {
                    Platform.runLater(() -> Utils.showErrorAlert(
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
                Platform.runLater(() -> Utils.showErrorAlert(
                        "Error Resuming Debugger Execution",
                        "Failed to resume debugger execution from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    RunResultsDTO context = GsonFactory.getGson().fromJson(responseBodyString, RunResultsDTO.class);
                    Platform.runLater(() -> debuggerWindowController.updateRunResultsAndFinishExecutionModeIfNeeded(context, ExecutionMode.DEBUG));
                    if (context.isFinished()) {
                        Platform.runLater(() -> finishExecutionMode(ExecutionMode.DEBUG));
                    } else {
                        fetchAndHighlightNextInstructionToExecute();
                    }

                }
                else {
                    ErrorDTO error = GsonFactory.getGson().fromJson(responseBodyString, ErrorDTO.class);
                    Platform.runLater(() -> handleError(error, ExecutionMode.DEBUG));
                }

                clientController.updateUserInfo();

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
                Platform.runLater(() -> Utils.showErrorAlert(
                        ("HTTP " + response.code() + " Error"),
                        ("Failed to update instruction breakpoint on server"),
                        responseBodyString));
            }
        } catch (Exception e) {
            Platform.runLater(() -> Utils.showErrorAlert(
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
                Platform.runLater(() -> Utils.showErrorAlert(
                        "Step Over Error",
                        "Failed to Step Over in debugging session from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    RunResultsDTO context = GsonFactory.getGson().fromJson(responseBodyString, RunResultsDTO.class);
                    Platform.runLater(() -> debuggerWindowController.updateRunResultsAndFinishExecutionModeIfNeeded(context, ExecutionMode.DEBUG));
                    if (context.isFinished()) {
                        Platform.runLater(() -> finishExecutionMode(ExecutionMode.DEBUG));
                    } else {
                        fetchAndHighlightNextInstructionToExecute();
                    }

                }
                else {
                    ErrorDTO error = GsonFactory.getGson().fromJson(responseBodyString, ErrorDTO.class);
                    Platform.runLater(() -> handleError(error, ExecutionMode.DEBUG));
                }

                clientController.updateUserInfo();

                response.close();
            }
        });
    }

    public void setUserInfo(UserDTO userDTO) {
        userInfoBannerController.updateUserInfo(userDTO);
    }

    public void highlightInstructionsByArchitecture(String architecture) {
        instructionsWindowController.highlightInstructionsByArchitecture(architecture);
    }

    public void reRunSelectedHistory(RunResultsDTO selectedRun) {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_INPUTS_NAMES);

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> Utils.showErrorAlert(
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
                        instructionsWindowController.setProgramDegree(selectedRun.getDegree());
                        debuggerWindowController.setInputVariablesValues(selectedRun.getInputVariablesInitialValues());
                        debuggerWindowController.setProgramArchitecture(selectedRun.getArchitecture());
                    });
                }
                else {
                    Platform.runLater(() -> Utils.showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to fetch input names from server"),
                            null));
                }

                response.close();
            }
        });
    }

    private void startExecutionMode(ExecutionMode mode) {
        debuggerWindowController.startExecutionMode(mode);
        if(mode == ExecutionMode.DEBUG) {
            fetchAndHighlightNextInstructionToExecute();
        }
        instructionsWindowController.disableDegreeChoiceControls(true);
        backToDashboardButton.setDisable(true);
    }

    private void finishExecutionMode(ExecutionMode mode) {
        debuggerWindowController.finishExecutionMode(mode);
        if (mode == ExecutionMode.DEBUG) {
            instructionsWindowController.stopHighlightingNextInstructionToExecute();
        }

        instructionsWindowController.disableDegreeChoiceControls(false);
        backToDashboardButton.setDisable(false);
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
                    Platform.runLater(() -> Utils.showErrorAlert(
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
                        Platform.runLater(() -> Utils.showErrorAlert(
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

    public void activateDynamicExecutionDataWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/components/dynamicexecutiondatawindow/DynamicExecutionDataWindow.fxml"));
            Parent load = loader.load();
            DynamicExecutionDataWindowController controller = loader.getController();
            controller.setExecutionScreenController(this);
            controller.startExecutionDataWindowRefresher();
            Platform.runLater(() -> {
                Scene scene = new Scene(load, 250, 230);
                Stage showWindow = new Stage();
                showWindow.setTitle("Dynamic Execution Data");
                showWindow.setScene(scene);
                showWindow.setResizable(false);
                showWindow.show();
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
                    Platform.runLater(() -> Utils.showErrorAlert(
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
                        Platform.runLater(() -> Utils.showErrorAlert(
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

    public void onExecutionFinished(RunResultsDTO runResultsDTO) {
        Platform.runLater(() -> {
            debuggerWindowController.updateRunResultsAndFinishExecutionModeIfNeeded(runResultsDTO, ExecutionMode.REGULAR);
            finishExecutionMode(ExecutionMode.REGULAR);
        });
    }

}
