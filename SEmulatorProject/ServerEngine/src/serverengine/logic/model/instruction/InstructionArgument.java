package serverengine.logic.model.instruction;

import types.ArgumentType;

public enum InstructionArgument {
    JNZ_LABEL(ArgumentType.LABEL, "JNZLabel"),
    GOTO_LABEL(ArgumentType.LABEL, "gotoLabel"),
    ASSIGNED_VARIABLE(ArgumentType.VARIABLE, "assignedVariable"),
    CONSTANT_VALUE(ArgumentType.CONSTANT, "constantValue"),
    JZ_LABEL(ArgumentType.LABEL, "JZLabel"),
    JE_CONSTANT_LABEL(ArgumentType.LABEL,  "JEConstantLabel"),
    VARIABLE_NAME(ArgumentType.VARIABLE, "variableName"),
    JE_VARIABLE_LABEL(ArgumentType.LABEL,  "JEVariableLabel"),
    FUNCTION_NAME(ArgumentType.NAME, "functionName"),
    FUNCTION_ARGUMENTS(ArgumentType.COMMA_SEPERATED_ARGUMENTS, "functionArguments"),
    JE_FUNCTION_LABEL(ArgumentType.LABEL, "JEFunctionLabel");

    private final ArgumentType type;
    private final String nameInXml;

    InstructionArgument(ArgumentType type, String nameInXML){
        this.type = type;
        this.nameInXml = nameInXML;
    }

    public ArgumentType getType(){
        return type;
    }

    public static InstructionArgument fromXmlNameFormat(String nameInXML) {
        for (InstructionArgument arg : values()) {
            if (arg.nameInXml.equals(nameInXML)) {
                return arg;
            }
        }

        throw new IllegalArgumentException("Unknown argument: " + nameInXML);
    }
}
