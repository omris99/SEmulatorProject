package gui.components.programswindow;

import gui.components.availablefunctionstable.AvailableFunctionsTableController;
import gui.components.availablefunctionstable.AvailableFunctionsTableRefresher;
import gui.components.availableprogramstable.AvailableProgramsTableController;
import javafx.fxml.FXML;

public class ProgramsWindowController {
    @FXML
    AvailableFunctionsTableController availableFunctionsTableController;
    @FXML
    AvailableProgramsTableController availableProgramsTableController;

    public void startAvailableFunctionsTableRefresher() {
        availableFunctionsTableController.startTableRefresher();
    }
    public void startAvailableProgramsTableRefresher() {
        availableProgramsTableController.startTableRefresher();
    }
}
