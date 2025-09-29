package gui.components.executionstatewindow;

import dto.RunResultsDTO;
import gui.components.variablesvaluetable.VariablesValueTableController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ExecutionStateWindowController {

    @FXML
    private VariablesValueTableController variablesValueTableController;
    @FXML
    private Label cyclesCounter;

    public void updateTableAndCycles(RunResultsDTO runResultsDTO) {
        variablesValueTableController.updateTable(runResultsDTO);
        cyclesCounter.setText(String.format("%d", runResultsDTO.getTotalCyclesCount()));
    }

    public void reset() {
        variablesValueTableController.reset();
        cyclesCounter.setText("-");
    }

    public void unmarkHighlightedLines(){
        variablesValueTableController.unmarkHighlightedLines();
    }
}
