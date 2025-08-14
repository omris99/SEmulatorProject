package logic.model.instruction;

import logic.model.variable.VariableOld;

import java.util.Map;

public class InstructionOld {
    private InstructionData details;
    private String label;
    private final Map<InstructionArgument, String> arguments;
    private VariableOld variable;
    private String instructionDisplayFormat;

    public InstructionOld(InstructionData details, VariableOld variable, String label, Map<InstructionArgument, String> arguments) {
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
                label != null ? label : "",
                instructionDisplayFormat,
                details.getCycles());
    }

    private void setInstructionDisplayFormat() {
        switch(details) {
            case INCREASE:
                instructionDisplayFormat = String.format("%s <- %s + 1", variable, variable);
                break;
            case DECREASE:
                instructionDisplayFormat = String.format("%s <- %s - 1", variable, variable);
                break;
            case JUMP_NOT_ZERO:
                instructionDisplayFormat = String.format("IF %s != 0 GOTO %s", variable, arguments.get(InstructionArgument.JNZ_LABEL));
                break;
            case NEUTRAL:
                instructionDisplayFormat = String.format("%s <- %s", variable, variable);
                break;
            case ZERO_VARIABLE:
                instructionDisplayFormat = String.format("%s <- 0", variable);
                break;
            case GOTO_LABEL:
                instructionDisplayFormat = String.format("GOTO %s", arguments.get(InstructionArgument.GOTO_LABEL));
                break;
            case ASSIGNMENT:
                instructionDisplayFormat = String.format("%s <- %s", variable, arguments.get(InstructionArgument.ASSIGNED_VARIABLE));
                break;
            case CONSTANT_ASSIGNMENT:
                instructionDisplayFormat = String.format("%s <- %s", variable, arguments.get(InstructionArgument.CONSTANT_VALUE));
                break;
            case JUMP_ZERO:
                instructionDisplayFormat = String.format("IF %s = 0 GOTO %s", variable, arguments.get(InstructionArgument.JZ_LABEL));
                break;
            case JUMP_EQUAL_CONSTANT:
                instructionDisplayFormat = String.format("IF %s = %s GOTO %s", variable, arguments.get(InstructionArgument.JE_CONSTANT_LABEL), arguments.get(InstructionArgument.CONSTANT_VALUE));
                break;
            case JUMP_EQUAL_VARIABLE:
                instructionDisplayFormat = String.format("IF %s = %s GOTO %s", variable, arguments.get(InstructionArgument.JE_VARIABLE_LABEL), arguments.get(InstructionArgument.VARIABLE_NAME));
                break;
            default:
                throw new IllegalArgumentException("Unknown instruction: " + details);
        }
    }

    public String getLabel() {
        return label;
    }

    public boolean isLabled(){
        return label != null;
    }

    public VariableOld getVariable(){
        return variable;
    }

    public Map<InstructionArgument, String> getArguments() {
        return arguments;
    }
}

