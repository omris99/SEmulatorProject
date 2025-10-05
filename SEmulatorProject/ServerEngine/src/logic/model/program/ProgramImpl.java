package logic.model.program;

import dto.DTO;
import dto.ProgramDTO;
import logic.exceptions.NumberNotInRangeException;
import logic.instructiontree.InstructionsTree;
import logic.model.argument.Argument;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.Instruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionWithArguments;
import logic.model.instruction.Instructions;

import java.util.*;
import java.util.stream.Collectors;

public class ProgramImpl extends AbstractProgram {
    public ProgramImpl(String name) {
        super(name);
    }

    @Override
    public Label validate(){
        Set<Label> labels = instructions.getLabels();

        for (Instruction instruction : instructions.getInstructionsList()) {
            if (instruction instanceof InstructionWithArguments) {
                Map<InstructionArgument, Argument> arguments = ((InstructionWithArguments) instruction).getArguments();
                for (Argument argument : arguments.values()) {
                    if (argument instanceof Label) {
                        if (!(labels.contains(argument) || argument.equals(FixedLabel.EXIT))) {
                            return (Label) argument;
                        }
                    }
                }
            }
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public Program getExpandedProgram(int degree) {

        if(degree > getMaximalDegree()){
            throw new NumberNotInRangeException(degree);
        }
        else if(degree == 0){
            return this;
        }
        else {
            ProgramImpl expandedProgram = new ProgramImpl(getName());
            for (Instruction instruction : instructions.getInstructionsList()) {
                expandedProgram.addInstruction(instruction.clone());
            }

            for(int i = 0; i < degree; i++){
                expandedProgram.instructions.expand();
            }

            return expandedProgram;
        }
    }

    public void setFunctionsNames(List<String> functionsNames) {
        this.functionsNames = functionsNames;
    }
}
