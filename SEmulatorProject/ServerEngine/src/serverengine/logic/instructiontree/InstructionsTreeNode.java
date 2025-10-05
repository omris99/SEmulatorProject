package serverengine.logic.instructiontree;

import clientserverdto.InstructionDTO;

import java.util.LinkedList;
import java.util.List;

public class InstructionsTreeNode {
    private InstructionDTO instruction;
    private final List<InstructionsTreeNode> children;

    public InstructionsTreeNode(InstructionDTO instruction) {
        this.instruction = instruction;
        this.children = new LinkedList<>();
    }

    public InstructionsTreeNode() {
        this.children = new LinkedList<>();
    }

    public void addChildNode(InstructionsTreeNode child) {
        this.children.add(child);
    }

    public List<InstructionsTreeNode> getChildrenNodes() {
        return children;
    }

    public InstructionDTO getInstruction() {
        return instruction;
    }
}
