package model;

import exceptions.UnknownLabelReferenceExeption;

import java.util.*;

public class Instructions {
    private List<Instruction> instructions;
    private List<String> instructionsLabels;
    private Set<Variable> instructionsInputs;

    public Instructions() {
        this.instructions = new ArrayList<Instruction>();
        this.instructionsLabels = new ArrayList<>();
        this.instructionsInputs = new LinkedHashSet<Variable>();
    }

    public void add(Instruction instruction) {
        this.instructions.add(instruction);
        Variable variable = instruction.getVariable();

        if(variable.isInputVariable()){
            this.instructionsInputs.add(variable);
        }

        if(instruction.isLabled()) {
            this.instructionsLabels.add(instruction.getLabel());
        }
    }

    public List<String> getLabels() {
        return instructionsLabels;
    }

    public Set<Variable> getInputs() {
        return instructionsInputs;
    }

    public List<Instruction> getInstructionsList() {
        return instructions;
    }
}
