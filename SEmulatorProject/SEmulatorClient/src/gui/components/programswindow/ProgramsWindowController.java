package gui.components.programswindow;

import gui.components.availablefunctionstable.AvailableFunctionsTableController;
import gui.components.availablefunctionstable.AvailableFunctionsTableRefresher;
import javafx.fxml.FXML;

public class ProgramsWindowController {
    @FXML
    AvailableFunctionsTableController availableFunctionsTableController;

    public void startAvailableFunctionsTableRefresher() {
        availableFunctionsTableController.startTableRefresher();
    }
}
