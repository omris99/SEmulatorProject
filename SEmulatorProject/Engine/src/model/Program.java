package model;

import java.util.List;

public class Program {
    private String name;
    private List<Instruction> instructions;

    public Program(String name, List<Instruction> instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public List<String> getAllLabels(){
        return null;
    }

    public List<String> getAllInputsNames(){
        return null;
    }

    public List<Instruction> getInstructions()
    {
        return instructions;
    }
}
