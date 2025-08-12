package model;

import java.util.List;
import java.util.Set;

public class Program {
    private String name;
    private Instructions instructions;

    public Program(String name, Instructions instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public List<String> getAllInstructionsLabels(){
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
