package gui.components.dynamicexecutiondatawindow;

import clientserverdto.RunResultsDTO;
import gui.screens.execution.ExecutionScreenController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import types.modeltypes.ArchitectureType;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DynamicExecutionDataWindowController implements Closeable {
    private Timer timer;
    private TimerTask executionDataWindowRefresher;


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

    public void updateExecutionDataWindow(RunResultsDTO runResultsDTO) {
        Map<ArchitectureType, Long> performedInstructionsCountByArchitecture = runResultsDTO.getPerformedInstructionsCountByArchitecture();

        Platform.runLater(() -> {
            archOneLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.getOrDefault(ArchitectureType.ONE, 0L)));
            archTwoLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.getOrDefault(ArchitectureType.TWO, 0L)));
            archThreeLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.getOrDefault(ArchitectureType.THREE, 0L)));
            archFourLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.getOrDefault(ArchitectureType.FOUR, 0L)));
            cyclesLabel.setText(String.valueOf(runResultsDTO.getTotalCyclesCount()));
            totalLabel.setText(String.valueOf(performedInstructionsCountByArchitecture.values().stream().mapToLong(Long::longValue).sum()));
        });

        if (runResultsDTO.isFinished()){
            executionScreenController.onExecutionFinished(runResultsDTO);
            stopExecutionDataWindowRefresher();
        }
    }

    @Override
    public void close() throws IOException {
        if(executionDataWindowRefresher != null && timer != null) {
            System.out.println("executionDataWindowController: Closing and cancelling refresher.");
            executionDataWindowRefresher.cancel();
            timer.cancel();
            timer.purge();
            System.out.println("executionDataWindowController:  CLOSED .");
        }
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
}
