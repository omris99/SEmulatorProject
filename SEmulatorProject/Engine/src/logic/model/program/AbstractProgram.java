package logic.model.program;

import dto.DTO;
import dto.ProgramDTO;
import logic.instructiontree.InstructionsTree;
import logic.model.argument.Argument;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.Instruction;
import logic.model.instruction.Instructions;

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
                name,
                getProgramInputsNames(),
                getProgramLabelsNames(),
                getInstructions().stream().map(Instruction::getInstructionDisplayFormat).collect(Collectors.toList()),
                getInstructions().stream().map(Instruction::getInstructionDTO).collect(Collectors.toList()),
                instructions.getInstructionsTypeCount(),
                instructions.getDegree(),
                instructions.getMaximalDegree(),
                getProgramWorkVariablesNames(),
                functionsNames
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

}
