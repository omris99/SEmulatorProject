package gui.components.instructionstable;

import clientserverdto.InstructionDTO;
import gui.app.AnimationsManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import serverengine.logic.model.instruction.ArchitectureType;

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
    private ArchitectureType selectedArchitecture;
    private int nextInstructionToExecuteIndex;
    private final ObservableList<InstructionDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        selectedArchitecture = ArchitectureType.ONE;
        ColIndex.setCellValueFactory(new PropertyValueFactory<>("index"));
        colInstructionType.setCellValueFactory(new PropertyValueFactory<>("instructionType"));
        colLabel.setCellValueFactory(new PropertyValueFactory<>("label"));
        colInstructionDisplayFormat.setCellValueFactory(new PropertyValueFactory<>("displayFormat"));
        colCycle.setCellValueFactory(new PropertyValueFactory<>("cycles"));
        colArchitecture.setCellValueFactory(new PropertyValueFactory<>("architectureType"));


        InstructionsTable.setItems(data);

        InstructionsTable.setRowFactory(tv -> new TableRow<InstructionDTO>() {
            @Override
            protected void updateItem(InstructionDTO item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("highlighted", "next-instruction", "architecture-highlighted");

                if (item == null || empty) {
                    return;
                }

                boolean isHighlighted = highlightedSelection != null &&
                        item.getAssociatedArgumentsAndLabels().contains(highlightedSelection);

                boolean isNextInstruction = nextInstructionToExecuteIndex > 0 &&
                        item.getIndex() == nextInstructionToExecuteIndex;

                ArchitectureType instructionArchitecture = ArchitectureType.fromUserString(item.getArchitectureType());

                boolean isArchitectureHighlighted = instructionArchitecture.getNumber() > selectedArchitecture.getNumber();

                if (isNextInstruction) {
                    getStyleClass().add("next-instruction");
                    AnimationsManager.playFadeIn(tv, 200);
                } if (isHighlighted) {
                    getStyleClass().add("highlighted");
                }
                if (isArchitectureHighlighted) {
                    getStyleClass().add("architecture-highlighted");
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

    public void highlightInstructionsByArchitecture(String architecture) {
        this.selectedArchitecture = ArchitectureType.fromUserString(architecture);
        InstructionsTable.refresh();
    }

    public void stopHighlightingNextInstructionToExecute(){
        this.nextInstructionToExecuteIndex = 0;
        InstructionsTable.refresh();
    }
}
