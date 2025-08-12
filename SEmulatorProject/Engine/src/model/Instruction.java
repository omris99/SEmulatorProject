package model;

import java.util.List;

public class Instruction {
    private InstructionDetails details;
    private String label;
    private List<InstructionArgument> arguments;
    private Variable variable;
    private String instructionDisplayFormat;

    public Instruction(InstructionDetails details, Variable variable, String label, List<InstructionArgument> arguments) {
        this.details = details;
        this.variable = variable;
        this.label = label;
        this.arguments = arguments;
        setInstructionDisplayFormat();
    }

    @Override
    public String toString() {
        return String.format("(%s) [ %-4s] %s (%d)",
                details.getType().equals("basic") ? "B" : "S",
                label,
                instructionDisplayFormat,
                details.getCycles());
    }

    private void setInstructionDisplayFormat() {
        switch(details) {
            case INCREASE:
                instructionDisplayFormat = String.format("%s <- %s + 1", variable);
                break;
            case DECREASE:
                instructionDisplayFormat = String.format("%s <- %s - 1", variable);
                break;
            case JUMP_NOT_ZERO:
                instructionDisplayFormat = String.format("IF %s!=0 GOTO %s", arguments.get(1));
                break;
            case NEUTRAL:
                instructionDisplayFormat = String.format("%s <- %s", variable);
                break;
            case ZERO_VARIABLE:
                instructionDisplayFormat = String.format("%s <- 0", variable);
                break;
            case GOTO_LABEL:
                instructionDisplayFormat = String.format("GOTO %s", arguments.get(1));
                break;
            case ASSIGNMENT:
                instructionDisplayFormat = String.format("%s <- %s", variable, arguments.get(0));
                break;
            case CONSTANT_ASSIGNMENT:
                instructionDisplayFormat = String.format("%s <- %s", variable, arguments.get(1));
                break;
            case JUMP_ZERO:
                instructionDisplayFormat = String.format("IF %s=0 GOTO %s", variable, arguments.get(1));
                break;
            case JUMP_EQUAL_CONSTANT:
                instructionDisplayFormat = String.format("IF %s=%s GOTO %s", variable, arguments.get(1), arguments.get(3));
                break;
            case JUMP_EQUAL_VARIABLE:
                instructionDisplayFormat = String.format("IF %s=%s GOTO %s", variable, arguments.get(3), arguments.get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown instruction: " + details);
        }
    }

}

