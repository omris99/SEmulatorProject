package logic.instructiontree;

import logic.model.instruction.Instruction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InstructionsTreeNode {
    private Instruction instruction;
    private List<InstructionsTreeNode> children;

    public InstructionsTreeNode(Instruction instruction, List<Instruction> children) {
        this.instruction = instruction;
        this.children = createChildrenNodes(children);
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

    private List<InstructionsTreeNode> createChildrenNodes(List<Instruction> children){
        List<InstructionsTreeNode> childrenNodes = new ArrayList<>();
        for(Instruction child : children){
            childrenNodes.add(new InstructionsTreeNode(child, child.getChildren()));
        }

        return childrenNodes;
    }
}
