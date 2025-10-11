package gui.components.programswindow;

import clientserverdto.UploadedProgramDTO;
import gui.components.availablefunctionstable.AvailableFunctionsTableController;
import gui.components.availableprogramstable.AvailableProgramsTableController;
import gui.dashboard.DashBoardController;
import javafx.fxml.FXML;

import java.io.Closeable;
import java.io.IOException;

public class ProgramsWindowController implements Closeable {
    @FXML
    private AvailableFunctionsTableController availableFunctionsTableController;
    @FXML
    private AvailableProgramsTableController availableProgramsTableController;

    @FXML
    private DashBoardController dashBoardController;


    @FXML
    public void initialize() {
        availableFunctionsTableController.setProgramsWindowController(this);
        availableProgramsTableController.setProgramsWindowController(this);
    }

    public void startAvailableFunctionsTableRefresher() {
        availableFunctionsTableController.startTableRefresher();
    }

    public void startAvailableProgramsTableRefresher() {
        availableProgramsTableController.startTableRefresher();
    }

    public void executeProgramButtonClicked(UploadedProgramDTO selectedProgram){
        dashBoardController.executeProgramButtonClicked(selectedProgram);
    }

    public void setDashBoardController(DashBoardController dashBoardController) {
        this.dashBoardController = dashBoardController;
    }

    @Override
    public void close() throws IOException {
        availableFunctionsTableController.close();
        availableProgramsTableController.close();
    }
}
