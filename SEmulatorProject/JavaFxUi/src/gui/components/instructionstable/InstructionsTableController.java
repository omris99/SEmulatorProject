package gui.components.instructionstable;

import dto.InstructionDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;

public class InstructionsTableController {

    @FXML private TableView<InstructionDTO> InstructionsTable;
    @FXML private TableColumn<InstructionDTO, Integer> ColIndex;
    @FXML private TableColumn<InstructionDTO, String> colInstructionType;
    @FXML private TableColumn<InstructionDTO, String> colLabel;
    @FXML private TableColumn<InstructionDTO, String> colInstructionDisplayFormat;
    @FXML private TableColumn<InstructionDTO, Integer> colCycle;

    private String highlightedSelection;
    private final ObservableList<InstructionDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        ColIndex.setCellValueFactory(new PropertyValueFactory<>("index"));
        colInstructionType.setCellValueFactory(new PropertyValueFactory<>("instructionType"));
        colLabel.setCellValueFactory(new PropertyValueFactory<>("label"));
        colInstructionDisplayFormat.setCellValueFactory(new PropertyValueFactory<>("displayFormat"));
        colCycle.setCellValueFactory(new PropertyValueFactory<>("cycles"));

        InstructionsTable.setItems(data);

        InstructionsTable.setRowFactory(tv -> new TableRow<InstructionDTO>() {
            @Override
            protected void updateItem(InstructionDTO item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    getStyleClass().remove("highlighted");
                } else {
                    if (highlightedSelection != null &&
                            item.getAssociatedArgumentsAndLabels().contains(highlightedSelection)) {
                        if (!getStyleClass().contains("highlighted")) {
                            getStyleClass().add("highlighted");
                        }
                    } else {
                        getStyleClass().remove("highlighted");
                    }
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

}
