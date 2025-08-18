package logic.model.program;

import dto.DTO;
import dto.ProgramDTO;
import logic.exceptions.NumberNotInRangeException;
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
import java.util.stream.Collectors;

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

//    private Program expandProgram(int degree){
//        ProgramImpl expandedProgram = new ProgramImpl(name);
//        for (Instruction instruction : instructions.getInstructionsList()) {
//            expandedProgram.addInstruction(instruction.clone());
//        }
//
//        for(int i = 0; i < degree; i++){
//            expandedProgram.instructions.expand();
//        }
//
//        return expandedProgram;
//    }

    @Override
    public Program expand(int degree) {

        if(degree > getMaximalDegree()){
            throw new NumberNotInRangeException(degree);
        }
        else if(degree == 0){
            return this;
        }
        else {
            ProgramImpl expandedProgram = new ProgramImpl(name);
            for (Instruction instruction : instructions.getInstructionsList()) {
                expandedProgram.addInstruction(instruction.clone());
            }

            for(int i = 0; i < degree; i++){
                expandedProgram.instructions.expand();
            }

            return expandedProgram;
        }
    }

    @Override
    public int getMaximalDegree(){
        return instructions.getMaximalDegree();
    }

    @Override
    public DTO createDTO() {
        return new ProgramDTO(
                name,
                getProgramInputsNames(),
                getProgramLabelsNames(),
                getInstructions().stream().map(Instruction::getInstructionDisplayFormat).collect(Collectors.toList()));
    }


    private List<String> getProgramLabelsNames() {
        List<String> programLabelsNames = getAllInstructionsLabels().stream()
                .filter(label -> !label.equals(FixedLabel.EXIT))
                .map(Argument::getRepresentation)
                .collect(Collectors.toList());

        if (getAllInstructionsLabels().contains(FixedLabel.EXIT)) {
            programLabelsNames.add(FixedLabel.EXIT.getRepresentation());
        }

        return programLabelsNames;
    }
    private List<String> getProgramInputsNames() {
        return getAllInstructionsInputs().stream().map(Argument::getRepresentation).collect(Collectors.toList());
    }
}
