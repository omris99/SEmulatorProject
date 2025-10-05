package gui.execution;

import dto.ProgramDTO;
import dto.UploadedProgramDTO;
import gui.components.debuggerwindow.DebuggerWindowController;
import gui.components.instructionswindow.InstructionsWindowController;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.fxml.FXML;
import logic.json.GsonFactory;
import okhttp3.*;

import java.io.IOException;

import static gui.app.ClientManager.showErrorAlert;

public class ExecutionScreenController {
    @FXML
    private InstructionsWindowController instructionsWindowController;
    @FXML
    private DebuggerWindowController debuggerWindowController;

    @FXML
    public void initialize() {
        instructionsWindowController.setExecutionScreenController(this);
    }

    public void setProgramToExecute(UploadedProgramDTO selectedProgram) {
        instructionsWindowController.onProgramLoaded(selectedProgram.getProgram());
        resetComponents();
    }

    private void resetComponents() {
        debuggerWindowController.reset();
//        historyWindowController.reset();
//        displayCommandsBarController.disableTreeTableViewAndSpecificExpansionButton(false);
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

}
