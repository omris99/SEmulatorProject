package gui.components.instructionstablewithbreakpoints;

import dto.InstructionDTO;
import gui.app.AnimationsManager;
import gui.components.instructionstable.InstructionsTableController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class InstructionsTableWithBreakpointsController extends InstructionsTableController {
    @FXML
    private TableColumn<InstructionDTO, Boolean> colBreakpoint;

    @Override
    @FXML
    public void initialize() {
        colBreakpoint.setCellValueFactory(param -> {
            InstructionDTO instr = param.getValue();
            return new SimpleBooleanProperty(instr.getIsBreakpointSet());
        });
        colBreakpoint.setCellFactory(tc -> new CheckBoxTableCell<>());
        super.initialize();
    }



}
