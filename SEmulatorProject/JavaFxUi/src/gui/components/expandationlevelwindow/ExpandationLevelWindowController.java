package gui.components.expandationlevelwindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ExpandationLevelWindowController {

    @FXML
    private TextField currentDegreeField;

    @FXML
    private VBox expandationLevelWindow;

    @FXML
    private Label maximalDegreeLabel;

    public void setMaximalDegree(int maximalDegree) {
        maximalDegreeLabel.setText(String.valueOf(maximalDegree));
    }

}
