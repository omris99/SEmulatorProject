package gui.components.architectureselector;

import gui.components.debuggerwindow.DebuggerWindowController;
import gui.components.toolbar.InstructionsWindowToolbarController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import types.ArchitectureType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArchitectureSelectorController {

    @FXML
    private ComboBox<String> architectureComboBox;

    DebuggerWindowController debuggerWindowController;

    @FXML
    private void initialize() {
        updateOptions();
        architectureComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                debuggerWindowController.onArchitectureSelected(newVal);
            }
        });
    }

    private void updateOptions() {
        List<String> architectures = new LinkedList<>();
        Arrays.stream(ArchitectureType.values()).map(ArchitectureType::getUserString).forEach(architectures::add);
        architectureComboBox.getSelectionModel().clearSelection();
        architectureComboBox.getItems().clear();
        architectureComboBox.getItems().addAll(architectures);
    }

    public void setDebuggerWindowController(DebuggerWindowController debuggerWindowController) {
        this.debuggerWindowController = debuggerWindowController;
    }

    public void reset(){
        architectureComboBox.getSelectionModel().clearSelection();
        architectureComboBox.setValue(architectureComboBox.getItems().getFirst());
    }

    public String getSelectedArchitecture(){
        return architectureComboBox.getSelectionModel().getSelectedItem();
    }

    public void setSelectedArchitecture(String architecture){
        architectureComboBox.setValue(architecture);
    }
}
