package gui.components.instructionstreetable;

import dto.InstructionDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class InstructionsTreeTableController {

    @FXML
    private TreeTableView<InstructionDTO> instructionsTreeTable;

    @FXML
    private TreeTableColumn<InstructionDTO, String> colIndex;

    @FXML
    private TreeTableColumn<InstructionDTO, String> colInstructionType;

    @FXML
    private TreeTableColumn<InstructionDTO, String> colLabel;

    @FXML
    private TreeTableColumn<InstructionDTO, String> colInstructionDisplayFormat;

    @FXML
    private TreeTableColumn<InstructionDTO, String> colCycle;

    @FXML
    public void initialize() {
        colIndex.setCellValueFactory(param -> new SimpleStringProperty(
                String.valueOf(param.getValue().getValue().getIndex())
        ));
        colInstructionType.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getInstructionType())
        );
        colLabel.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getLabel())
        );
        colInstructionDisplayFormat.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getDisplayFormat())
        );
        colCycle.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getValue().getCycles()))
        );

        TreeItem<InstructionDTO> root = new TreeItem<>(new InstructionDTO());
        root.setExpanded(true);

        for(InstructionDTO instruction : instructionsTreeTable.getRoot().getChildren().stream().map(TreeItem::getValue).toList()) {
            TreeItem<InstructionDTO> parentInstruction = new TreeItem<>(instruction);
            for(InstructionDTO child : instruction.getChildren()){
                TreeItem<InstructionDTO> childInstruction = new TreeItem<>(child);
                parentInstruction.getChildren().add(childInstruction);
            }
            instructionsTreeTable.getRoot().getChildren().add(parentInstruction);
        }
        instructionsTreeTable.setRoot(root);
        instructionsTreeTable.setShowRoot(false);
    }
}
