package logic.model.program;

import logic.exceptions.UnknownLabelReferenceExeption;
import logic.model.argument.Argument;
import logic.model.instruction.Instruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionWithArguments;
import logic.model.instruction.Instructions;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.util.*;

public class ProgramImpl implements Program {
    private final String name;
    private final Instructions instructions;

    public ProgramImpl(String name) {
        this.name = name;
        this.instructions = new Instructions();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions.getInstructionsList();
    }

    @Override
    public void validate() throws UnknownLabelReferenceExeption {
        Set<Label> labels = instructions.getLabels();

        for (Instruction instruction : instructions.getInstructionsList()) {
            if (instruction instanceof InstructionWithArguments) {
                Map<InstructionArgument, Argument> arguments = ((InstructionWithArguments) instruction).getArguments();
                for (Argument argument : arguments.values()) {
                    if (argument instanceof Label) {
                        if (!(labels.contains(argument) || argument.equals(FixedLabel.EXIT))) {
                            throw new UnknownLabelReferenceExeption(argument.getRepresentation());
                        }
                    }
                }
            }
        }
    }

    @Override
    public int calculateMaxDegree() {
        // traverse all commands and find maximum degree
        return 0;
    }

    @Override
    public int calculateCycles() {
        // traverse all commands and calculate cycles
        return 0;
    }

    public Set<Label> getAllInstructionsLabels() {
        return instructions.getLabels();
    }

    public Set<Variable> getAllInstructionsInputs() {
        return instructions.getInputs();
    }

    public Set<Variable> getAllInstructionsWorkVariables() {
        return instructions.getWorkVariables();
    }

    @Override
    public void expand(int degree) {
        if(degree == 0){
            return;
        }
        else {
            for(int i = 0; i < degree; i++){
                instructions.expand();
            }
        }
    }
}
