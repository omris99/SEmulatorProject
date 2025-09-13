package gui.popups.showallvariables;

import dto.RunResultsDTO;
import gui.components.variablesvaluetable.VariablesValueTableController;
import javafx.fxml.FXML;

public class ShowAllVariablesController {

    @FXML
    private VariablesValueTableController allVariablesTableController;

    public void setRunResults(RunResultsDTO runResultsDTO) {
        allVariablesTableController.updateTable(runResultsDTO);
    }

}
