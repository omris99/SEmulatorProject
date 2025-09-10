package gui.components.summaryline;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SummaryLineController {

    @FXML
    private TextField basicInstructionsCount;

    @FXML
    private TextField syntheticInstructionsCount;

    public void setSummaryLineValues(int basicInstructionsCount, int syntheticInstructionsCount) {
        Platform.runLater(() -> {
            this.basicInstructionsCount.setText(String.valueOf(basicInstructionsCount));
            this.syntheticInstructionsCount.setText(String.valueOf(syntheticInstructionsCount));
        });
    }

}
