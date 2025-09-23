package logic.model.instruction;

import logic.instructiontree.InstructionsTree;
import logic.instructiontree.InstructionsTreeNode;
import logic.model.argument.Argument;
import logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableType;

import java.io.Serializable;
import java.util.*;

public class Instructions implements Serializable {
    private final List<Instruction> instructions;
    private final Set<Label> instructionsLabels;
    private final Set<Variable> instructionsInputs;
    private final Set<Variable> instructionsWorkVariables;
    private int degree;
    private int cycles;

    public Instructions() {
        this.instructions = new LinkedList<>();
        this.instructionsLabels = new LinkedHashSet<>();
        this.instructionsInputs = new LinkedHashSet<>();
        this.instructionsWorkVariables = new LinkedHashSet<>();
        this.cycles = 0;
    }

    public void add(Instruction instruction) {
        add(instruction, instructions.size());
    }

    public void add(Instruction instruction, int indexToAdd) {
        this.instructions.add(indexToAdd, instruction);
        instruction.setIndex(indexToAdd + 1);
        cycles += instruction.getCycles();
        Variable variable = instruction.getVariable();

        if (variable.getType() == VariableType.INPUT) {
            this.instructionsInputs.add(variable);
        } else if (variable.getType() == VariableType.WORK) {
            this.instructionsWorkVariables.add(variable);
        }

        if (instruction.getLabel() != FixedLabel.EMPTY) {
            this.instructionsLabels.add(instruction.getLabel());
        }

        if (instruction instanceof InstructionWithArguments) {
            Collection<Argument> arguments = ((InstructionWithArguments) instruction).getArguments().values();
            for (Argument argument : arguments) {
                if (argument instanceof Variable) {
                    if (((Variable) argument).getType() == VariableType.INPUT) {
                        this.instructionsInputs.add((Variable) argument);
                    }
                } else if (argument instanceof Label) {
                    if (argument == FixedLabel.EXIT) {
                        this.instructionsLabels.add((Label) argument);
                    }
                }
                else if (argument instanceof CommaSeperatedArguments) {
                    this.instructionsInputs.addAll(((CommaSeperatedArguments) argument).detectInputVariables());
                }
            }
        }
    }

    public void addListOfInstructions(List<Instruction> instructions, int index) {
        this.instructions.remove(index);
        int indexToAdd = index;
        for (Instruction instruction : instructions) {
            add(instruction, indexToAdd);
            indexToAdd++;
        }
    }

    public Set<Label> getLabels() {
        return instructionsLabels;
    }

    public Set<Variable> getInputs() {
        return instructionsInputs;
    }

    public Set<Variable> getWorkVariables() {
        return instructionsWorkVariables;
    }

    public List<Instruction> getInstructionsList() {
        return instructions;
    }

    public int getMaximalDegree() {
        return instructions.stream().map(Instruction::getDegree).max(Comparator.naturalOrder()).get();
    }

    private InstructionsTreeNode createInsructionNode(Instruction instruction) {
        InstructionsTreeNode instructionNode = new InstructionsTreeNode(instruction);
        List<Instruction> children = new LinkedList<>();
        if(instruction instanceof ExpandableInstruction) {
            List<Instruction> expanded = ((ExpandableInstruction) instruction)
                    .expand(getMaxLabelIndex(), getMaxWorkVariableIndex(), instruction.getLabel());
            expanded.forEach(child -> children.add(child));
        }
        else{
            return instructionNode;
        }

        for(Instruction child : children){
            instructionNode.addChild(createInsructionNode(child));
//            InstructionsTreeNode childNode = createInsructionNode(instruction, child);
//            parentNode.addChild(childNode);
        }

        return instructionNode;
    }


    public void expand() {

        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction instanceof ExpandableInstruction) {
                List<Instruction> expanded = ((ExpandableInstruction) instruction)
                        .expand(getMaxLabelIndex(), getMaxWorkVariableIndex(), instruction.getLabel());
                expanded.forEach(newInstruction -> {
                    newInstruction.setParent(instruction);
                });
                addListOfInstructions(expanded, i);
                i += expanded.size() - 1;
            }

        }

        resetIndexes();
        degree++;
    }


    private int getMaxLabelIndex() {
        return instructionsLabels.stream().map(Argument::getIndex).max(Comparator.naturalOrder()).orElse(0);
    }

    private int getMaxWorkVariableIndex() {
        return instructionsWorkVariables.stream().map(Argument::getIndex).max(Comparator.naturalOrder()).orElse(0);
    }

    private int getMaxInputVariableIndex() {
        return instructionsInputs.stream().map(Argument::getIndex).max(Comparator.naturalOrder()).orElse(0);
    }

    public void resetIndexes() {
        int index = 1;

        for (Instruction instruction : instructions) {
            instruction.setIndex(index);
            index++;
        }
    }

    public Map<InstructionType, Integer> getInstructionsTypeCount() {
        Map<InstructionType, Integer> instructionsTypeCount = new HashMap<>();
        instructionsTypeCount.put(InstructionType.BASIC, 0);
        instructionsTypeCount.put(InstructionType.SYNTHETIC, 0);
        for (Instruction instruction : instructions) {
            if (instruction.getType() == InstructionType.BASIC) {
                instructionsTypeCount.put(InstructionType.BASIC, instructionsTypeCount.get(InstructionType.BASIC) + 1);
            }
            else {
                instructionsTypeCount.put(InstructionType.SYNTHETIC, instructionsTypeCount.get(InstructionType.SYNTHETIC) + 1);
            }
        }

        return instructionsTypeCount;
    }

    public int getDegree() {
        return degree;
    }

    public int getTotalCycles(){
        return cycles;
    }

    public void updateInstructionBreakpoint(int instructionIndex, boolean isSet){
        Instruction instruction = instructions.stream().filter(instr -> instr.getIndex() == instructionIndex).findFirst().orElse(null);
        if(instruction != null){
            instruction.setBreakpoint(isSet);
        }
    }

    public InstructionsTree getInstructionsTree(){
        InstructionsTree tree = new InstructionsTree();
        InstructionsTreeNode root = tree.getRoot();

        Map<Instruction, InstructionsTreeNode> parents = new HashMap<>();

        for(Instruction instruction : instructions) {

            InstructionsTreeNode node = new InstructionsTreeNode(instruction);
            Instruction parent = instruction.getParent();
            while (parent != null) {
                if (parents.containsKey(parent)) {
                    if(!parents.get(parent).getChildren().contains(node)) {
                        parents.get(parent).addChild(node);
                    }
                } else {
                    InstructionsTreeNode parentNode = new InstructionsTreeNode(parent);
                    if(!parentNode.getChildren().contains(node)) {
                        parentNode.addChild(node);
                    }
                    parents.put(parent, parentNode);
                }

                node = parents.get(parent);
                parent = parent.getParent();
            }

            if (!root.getChildren().contains(node)) {
                root.addChild(node);
            }
        }

        return tree;
    }

    public void createParentsMap(Map<Instruction, InstructionsTreeNode> parents){
        for(Instruction child : instructions){
            InstructionsTreeNode childNode = new InstructionsTreeNode(child);
            Instruction parent = child.getParent();
            while (parent != null){
                if(parents.containsKey(parent)){
                    parents.get(parent).addChild(childNode);
                }
                else{
                    parents.put(parent, childNode);
                }
            }
            if(child.getParent() != null) {
                if(parents.containsKey(child.getParent())){
                    parents.get(child.getParent()).addChild(childNode);
                    parents.put(child, childNode);
                }
                else{

                }
                createParentsMap(parents);
            }
        }
    }
}