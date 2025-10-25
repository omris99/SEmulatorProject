package clientserverdto.instructiontree;

public class InstructionsTree {
    InstructionsTreeNode root;

    public InstructionsTree() {
        this.root = new InstructionsTreeNode();
    }

    public InstructionsTreeNode getRoot() {
        return root;
    }
}
