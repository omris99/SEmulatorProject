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

public class ProgramImpl implements Program {
    private final String name;
    private final Instructions instructions;
    private List<String> functionsNames;
    private Map<Integer, Program> cachedExpandations = new HashMap<>();


    public ProgramImpl(String name) {
        this.name = name.trim();
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
    public Program getExpandedProgram(int degree) {

        if(degree > getMaximalDegree()){
            throw new NumberNotInRangeException(degree);
        }
        else if(degree == 0){
            return this;
        }
        else {
//            if(cachedExpandations.containsKey(degree)){
//                return cachedExpandations.get(degree);
//            }

            ProgramImpl expandedProgram = new ProgramImpl(name);
            for (Instruction instruction : instructions.getInstructionsList()) {
                expandedProgram.addInstruction(instruction.clone());
            }

            for(int i = 0; i < degree; i++){
                expandedProgram.instructions.expand();
            }

            cachedExpandations.put(degree, expandedProgram);
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
                getInstructions().stream().map(Instruction::getInstructionDisplayFormat).collect(Collectors.toList()),
                getInstructions().stream().map(Instruction::getInstructionDTO).collect(Collectors.toList()),
                instructions.getInstructionsTypeCount(),
                instructions.getDegree(),
                instructions.getMaximalDegree(),
                getAllInstructionsWorkVariables(),
                getFunctionsNames()
        );
    }


    private List<String> getProgramLabelsNames() {
        List<String> programLabelsNames = getAllInstructionsLabels().stream()
                .filter(label -> !label.equals(FixedLabel.EXIT)).sorted(Comparator.comparingInt(Label::getIndex))
                .map(Argument::getRepresentation)
                .collect(Collectors.toList());

        if (getAllInstructionsLabels().contains(FixedLabel.EXIT)) {
            programLabelsNames.add(FixedLabel.EXIT.getRepresentation());
        }

        return programLabelsNames;
    }

    private List<String> getProgramInputsNames() {
        return getAllInstructionsInputs().stream()
                .sorted(Comparator.comparingInt(Variable::getNumber))
                .map(Argument::getRepresentation).collect(Collectors.toList());
    }

    public int getDegree(){
        return instructions.getDegree();
    }
    private List<String> getFunctionsNames() {
        return functionsNames;
    }

    public void setFunctionsNames(List<String> functionsNames) {
        this.functionsNames = functionsNames;
    }
    public InstructionsTree getInstructionsTree() {return instructions.getInstructionsTree();}
}
