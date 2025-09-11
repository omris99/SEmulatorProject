package gui.components.inputrow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class InputRowController {

    @FXML
    private Label variableNameLabel;

    @FXML
    private TextField variableValueTextField;

    public void setName(String name) {
        variableNameLabel.setText(name);
    }

    public String getName() {
        return variableNameLabel.getText();
    }

    public void setValue(String value) {
        variableValueTextField.setText(value);
    }

    public String getValue() {
        return variableValueTextField.getText();
    }
}
