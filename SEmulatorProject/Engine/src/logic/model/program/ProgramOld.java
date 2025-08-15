package logic.model.program;

import logic.model.label.Label;
import logic.model.variable.Variable;
import logic.model.variable.VariableOld;
import logic.model.instruction.Instructions;

import java.util.Set;

public class ProgramOld {
    private String name;
    private Instructions instructions;

    public ProgramOld(String name, Instructions instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public Set<Label> getAllInstructionsLabels(){
        return instructions.getLabels();
    }

    public Set<Variable> getAllInputsNames(){
        return instructions.getInputs();
    }

    public Instructions getInstructions()
    {
        return instructions;
    }
}
