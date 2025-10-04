package gui.components.instructionstable;

import dto.InstructionDTO;
import gui.app.AnimationsManager;
import gui.components.instructionswindow.InstructionsWindowController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;

public class InstructionsTableController {

    @FXML
    private TableView<InstructionDTO> InstructionsTable;
    @FXML
    private TableColumn<InstructionDTO, Integer> ColIndex;
    @FXML
    private TableColumn<InstructionDTO, String> colInstructionType;
    @FXML
    private TableColumn<InstructionDTO, String> colLabel;
    @FXML
    private TableColumn<InstructionDTO, String> colInstructionDisplayFormat;
    @FXML
    private TableColumn<InstructionDTO, Integer> colCycle;

    @FXML
    private TableColumn<InstructionDTO, String> colArchitecture;


    private String highlightedSelection;
    private int nextInstructionToExecuteIndex;
    private final ObservableList<InstructionDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        ColIndex.setCellValueFactory(new PropertyValueFactory<>("index"));
        colInstructionType.setCellValueFactory(new PropertyValueFactory<>("instructionType"));
        colLabel.setCellValueFactory(new PropertyValueFactory<>("label"));
        colInstructionDisplayFormat.setCellValueFactory(new PropertyValueFactory<>("displayFormat"));
        colCycle.setCellValueFactory(new PropertyValueFactory<>("cycles"));
//        colArchitecture.setCellValueFactory(new PropertyValueFactory<>("architecture"));


        InstructionsTable.setItems(data);

        InstructionsTable.setRowFactory(tv -> new TableRow<InstructionDTO>() {
            @Override
            protected void updateItem(InstructionDTO item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("highlighted", "next-instruction");

                if (item == null || empty) {
                    return;
                }

                boolean isHighlighted = highlightedSelection != null &&
                        item.getAssociatedArgumentsAndLabels().contains(highlightedSelection);

                boolean isNextInstruction = nextInstructionToExecuteIndex > 0 &&
                        item.getIndex() == nextInstructionToExecuteIndex;

                if (isNextInstruction) {
                    getStyleClass().add("next-instruction");
                    AnimationsManager.playFadeIn(tv, 200);
                } else if (isHighlighted) {
                    getStyleClass().add("highlighted");
                }
            }
        });
    }

    public void setInstructions(List<InstructionDTO> instructions) {
        data.setAll(instructions);
    }

    public TableView<InstructionDTO> getTable() {
        return InstructionsTable;
    }

    public void highlightInstructionsWithSelection(String selection) {
        this.highlightedSelection = selection;
        InstructionsTable.refresh();
    }

    public void highlightNextInstructionToExecute(int index) {
        this.nextInstructionToExecuteIndex = index;
        InstructionsTable.refresh();
        Platform.runLater(() -> {
            data.stream()
                    .filter(instr -> instr.getIndex() == index)
                    .findFirst()
                    .ifPresent(instr -> InstructionsTable.scrollTo(instr));
        });
    }

    public void stopHighlightingNextInstructionToExecute(){
        this.nextInstructionToExecuteIndex = 0;
        InstructionsTable.refresh();
    }
}
