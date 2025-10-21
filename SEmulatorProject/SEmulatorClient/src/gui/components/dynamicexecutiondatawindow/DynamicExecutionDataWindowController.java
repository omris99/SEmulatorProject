package gui.components.dynamicexecutiondatawindow;

import clientserverdto.ExecutionStatus;
import clientserverdto.ExecutionStatusDTO;
import clientserverdto.RunResultsDTO;
import gui.screens.execution.ExecutionMode;
import gui.screens.execution.ExecutionScreenController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import types.modeltypes.ArchitectureType;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DynamicExecutionDataWindowController implements Closeable {
    private Timer timer;
    private TimerTask executionDataWindowRefresher;
    private boolean windowShown = false;


    @FXML
    private Label archFourLabel;

    @FXML
    private Label archOneLabel;

    @FXML
    private Label archThreeLabel;

    @FXML
    private Label archTwoLabel;

    @FXML
    private Label cyclesLabel;

    @FXML
    private Label totalLabel;

    private ExecutionScreenController executionScreenController;


    public void startExecutionDataWindowRefresher() {
        executionDataWindowRefresher = new DynamicExecutionDataWindowRefresher(
                this::updateExecutionDataWindow);
        timer = new Timer();
        timer.schedule(executionDataWindowRefresher, 500, 500);
    }

    public void updateExecutionDataWindow(ExecutionStatusDTO executionStatusDTO) {
        if (executionStatusDTO.getStatus() == ExecutionStatus.ERROR) {
            Platform.runLater(() ->
                    executionScreenController.handleError(executionStatusDTO.getError(), ExecutionMode.REGULAR));
            stopExecutionDataWindowRefresher();
            return;
        }

        if (executionStatusDTO.getStatus() == ExecutionStatus.RUNNING && !windowShown) {
            windowShown = true;
            Platform.runLater(this::showWindow);
        }

        RunResultsDTO runResultsDTO = executionStatusDTO.getLastRunResult();

        Map<ArchitectureType, Long> performedInstructionsCountByArchitecture = runResultsDTO.getPerformedInstructionsCountByArchitecture();

        Platform.runLater(() -> {
            archOneLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.getOrDefault(ArchitectureType.ONE, 0L)));
            archTwoLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.getOrDefault(ArchitectureType.TWO, 0L)));
            archThreeLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.getOrDefault(ArchitectureType.THREE, 0L)));
            archFourLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.getOrDefault(ArchitectureType.FOUR, 0L)));
            cyclesLabel.setText(String.valueOf(runResultsDTO.getTotalCyclesCount()));
            totalLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.values().stream().mapToLong(Long::longValue).sum()));
        });

        if (executionStatusDTO.getStatus() == ExecutionStatus.FINISHED) {
            executionScreenController.onExecutionFinished(runResultsDTO);
            stopExecutionDataWindowRefresher();
        }
    }

    @Override
    public void close() throws IOException {
        if (executionDataWindowRefresher != null && timer != null) {
            executionDataWindowRefresher.cancel();
            timer.cancel();
            timer.purge();
        }

        windowShown = false;
    }

    public void stopExecutionDataWindowRefresher() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void setExecutionScreenController(ExecutionScreenController executionScreenController) {
        this.executionScreenController = executionScreenController;
    }

    private void showWindow() {
        executionScreenController.showDynamicExecutionDataWindow();
    }
}

