package logic.model.program;

import logic.model.instruction.Instruction;
import logic.model.variable.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProgramImpl implements Program {
    private final String name;
    private final List<Instruction> instructions;

    public ProgramImpl(String name) {
        this.name = name;
        instructions = new ArrayList<>();
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
        return instructions;
    }

    @Override
    public boolean validate() {
        return false;
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

    public Set<String> getAllInstructionsLabels(){
        return null;
    }

    public Set<Variable> getAllInputsNames(){
        return null;
    }


}
