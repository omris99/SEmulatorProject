package gui.components.instructionstreetable;

import dto.InstructionDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import logic.instructiontree.InstructionsTree;
import logic.instructiontree.InstructionsTreeNode;
import logic.model.instruction.Instruction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    TreeItem<InstructionDTO> root = new TreeItem<>();


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

        root.setExpanded(true);
        instructionsTreeTable.setRoot(root);
        instructionsTreeTable.setShowRoot(false);
    }

    private TreeItem<InstructionDTO> createTreeItemRecursive(InstructionsTreeNode instruction) {
        List<InstructionsTreeNode> children = instruction.getChildren();
        TreeItem<InstructionDTO> item = new TreeItem<>(instruction.getInstruction().getInstructionDTO());

        if (!children.isEmpty()) {
            for (InstructionsTreeNode child : children) {
                item.getChildren().add(createTreeItemRecursive(child));
            }
        }

        return item;
    }


    public void setInstructions(InstructionsTree instructionsTree) {
        root.getChildren().clear();

        InstructionsTreeNode treeRoot = instructionsTree.getRoot();
        for(InstructionsTreeNode node : treeRoot.getChildren()) {
            TreeItem<InstructionDTO> childItem = createTreeItemRecursive(node);
            this.root.getChildren().add(childItem);
        }
    }
}
