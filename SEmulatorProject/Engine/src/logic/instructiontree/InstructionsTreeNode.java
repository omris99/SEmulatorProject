package logic.instructiontree;

import logic.model.instruction.Instruction;

import java.util.LinkedList;
import java.util.List;

public class InstructionsTreeNode {
    private Instruction instruction;
    private List<InstructionsTreeNode> children;

    public InstructionsTreeNode(Instruction instruction) {
        this.instruction = instruction;
        this.children = new LinkedList<>();
    }

    public InstructionsTreeNode() {
        this.children = new LinkedList<>();
    }

    public void addChild(InstructionsTreeNode child) {
        this.children.add(child);
    }

    public List<InstructionsTreeNode> getChildren() {
        return children;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
