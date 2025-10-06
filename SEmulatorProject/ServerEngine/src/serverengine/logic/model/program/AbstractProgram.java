package serverengine.logic.model.program;


import clientserverdto.DTO;
import clientserverdto.ProgramDTO;
import serverengine.logic.instructiontree.InstructionsTree;
import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.functionsrepo.ProgramsRepo;
import serverengine.logic.model.instruction.ArchitectureType;
import serverengine.logic.model.instruction.Instruction;
import serverengine.logic.model.instruction.Instructions;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractProgram implements Program {
    protected final String name;
    protected final Instructions instructions;
    protected List<String> functionsNames;

    public AbstractProgram(String name) {
        this.name = name.trim();
        this.instructions = new Instructions();
        this.functionsNames = new LinkedList<>();
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
    public Set<Label> getAllInstructionsLabels() {
        return instructions.getLabels();
    }

    @Override
    public Set<Variable> getAllInstructionsInputs() {
        return instructions.getInputs();
    }

    @Override
    public Set<Variable> getAllInstructionsWorkVariables() {
        return instructions.getWorkVariables();
    }

    @Override
    public int getMaximalDegree() {
        return instructions.getMaximalDegree();
    }

    @Override
    public DTO createDTO() {
        return new ProgramDTO(
                getRepresentation(),
                getProgramInputsNames(),
                getProgramLabelsNames(),
                getInstructions().stream().map(Instruction::getInstructionDisplayFormat).collect(Collectors.toList()),
                getInstructions().stream().map(Instruction::getInstructionDTO).collect(Collectors.toList()),
                instructions.getInstructionsArchitectureTypeCount(),
                instructions.getInstructionsTypeCount(),
                instructions.getDegree(),
                instructions.getMaximalDegree(),
                getProgramWorkVariablesNames(),
                functionsNames.stream().map(name -> ProgramsRepo.getInstance().getFunctionUserString(name)).toList()
        );
    }

    @Override
    public int getDegree() {
        return instructions.getDegree();
    }

    @Override
    public InstructionsTree getInstructionsTree() {
        return instructions.getInstructionsTree();
    }

    private List<String> getProgramInputsNames() {
        return getAllInstructionsInputs().stream()
                .sorted(Comparator.comparingInt(Variable::getNumber))
                .map(Argument::getRepresentation).collect(Collectors.toList());
    }

    private List<String> getProgramWorkVariablesNames() {
        return getAllInstructionsWorkVariables().stream()
                .sorted(Comparator.comparingInt(Variable::getNumber))
                .map(Argument::getRepresentation).collect(Collectors.toList());
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

    public ArchitectureType getMinimalArchitectureType() {
        ArchitectureType minimalArchitectureType = ArchitectureType.ONE;

        for(Instruction instruction : instructions.getInstructionsList()) {
            if(instruction.getArchitectureType().getNumber() > minimalArchitectureType.getNumber()){
                minimalArchitectureType = instruction.getArchitectureType();
            }
        }

        return minimalArchitectureType;
    }
}
