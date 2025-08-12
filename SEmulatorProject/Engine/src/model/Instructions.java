package model;

import exceptions.UnknownLabelReferenceExeption;

import java.util.*;

public class Instructions {
    private final List<Instruction> instructions;
    private List<String> instructionsLabels;
    private Set<Variable> instructionsInputs;

    public Instructions(List<Instruction> instructions) {
        this.instructions = instructions;
        this.instructionsLabels = new ArrayList<>();
        this.instructionsInputs = new LinkedHashSet<Variable>();

        for(Instruction instruction : instructions){
            Variable variable = instruction.getVariable();

            if(variable.isInputVariable()){
                this.instructionsInputs.add(variable);
            }

            if(instruction.isLabled()) {
                this.instructionsLabels.add(instruction.getLabel());
            }
        }

        for(Instruction instruction : instructions){
            Map<InstructionArgument, String> arguments = instruction.getArguments();
            if(arguments != null){
                for(InstructionArgument instructionArgument : arguments.keySet()){
                    if(instructionArgument.getType().equals("label")) {
                        String label = arguments.get(instructionArgument);
                        if(!instructionsLabels.contains(label) && !label.equals("EXIT")){
                            throw new UnknownLabelReferenceExeption(label);
                        }
                    }
                }
            }
        }
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
