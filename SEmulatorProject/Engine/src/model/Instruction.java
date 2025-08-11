package model;

import java.util.List;

public class Instruction {
    private InstructionDetails details;
    private String label;
    private List<InstructionArgument> arguments;
    private Variable Variable;

    public Instruction(InstructionDetails details, Variable Variable, String label, List<InstructionArgument> arguments) {
        this.details = details;
        this.Variable = Variable;
        this.label = label;
        this.arguments = arguments;
    }

}

