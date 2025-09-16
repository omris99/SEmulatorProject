package logic.model.program;

import dto.DTO;
import logic.model.argument.Argument;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.Instruction;
import logic.model.instruction.Instructions;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Function implements Program, Argument {
    private final String name;
    private final String userString;
    private final Instructions instructions;
    private final List<Function> functions;


    public Function(String name, String userString) {
        this.name = name;
        this.userString = userString;
        this.instructions = new Instructions();
        this.functions = new LinkedList<>();
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
    public Program getExpandedProgram(int degree) {
        return null;
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
    public Label validate() {
        return null;
    }

    @Override
    public int getMaximalDegree() {
        return 0;
    }

    @Override
    public DTO createDTO() {
        return null;
    }

    @Override
    public int getDegree() {
        return 0;
    }

    @Override
    public String getRepresentation() {
        return userString;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    public List<Function> getFunctions() {
        return functions;
    }
}
