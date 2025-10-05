package gui.components.executionstatewindow;

import clientserverdto.RunResultsDTO;
import gui.components.variablesvaluetable.VariablesValueTableController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
