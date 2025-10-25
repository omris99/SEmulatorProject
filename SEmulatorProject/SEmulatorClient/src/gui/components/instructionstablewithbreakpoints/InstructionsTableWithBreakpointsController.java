package gui.components.instructionstablewithbreakpoints;

import clientserverdto.InstructionDTO;
import gui.components.instructionstable.InstructionsTableController;
import gui.components.instructionswindow.InstructionsWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class InstructionsTableWithBreakpointsController extends InstructionsTableController {
    @FXML
    private TableColumn<InstructionDTO, Boolean> colBreakpoint;

    @FXML
    private InstructionsWindowController instructionsWindowController;

    List<Integer> breakpointInstructionIndexList = new ArrayList<>();

    @Override
    @FXML
    public void initialize() {
        colBreakpoint.setCellFactory(tc -> new TableCell<InstructionDTO, Boolean>() {

            private final Circle circle = new Circle(8, Color.RED);
            private final StackPane stack = new StackPane(circle);

            {
                stack.setOnMouseClicked(event -> {
                    if (!isEmpty()) {
                        InstructionDTO instr = getTableView().getItems().get(getIndex());
                        boolean newValue = !circle.isVisible();
                        circle.setVisible(newValue);
                        getTableView().getItems().set(getIndex(), onBreakpointToggled(instr, newValue));
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    InstructionDTO instr = getTableView().getItems().get(getIndex());

                    setGraphic(stack);
                    setStyle("-fx-alignment: CENTER;");
                    if (instr != null) {
                        circle.setVisible(instr.getIsBreakpointSet());
                    }
                }
            }
        });

        super.initialize();
    }

    private InstructionDTO onBreakpointToggled(InstructionDTO instr, boolean newValue) {
        return instructionsWindowController.onBreakpointToggled(instr.getIndex(), newValue);
    }

    public void setInstructionsWindowController(InstructionsWindowController instructionsWindowController) {
        this.instructionsWindowController = instructionsWindowController;
    }

    @Override
    public boolean shouldHighlight() {
        return true;
    }
}

