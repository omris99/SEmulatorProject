package logic.model.instruction;

public enum InstructionArgument {
    JNZ_LABEL("label", "JNZLabel"),
    GOTO_LABEL("label", "gotoLabel"),
    ASSIGNED_VARIABLE("variable", "assignedVariable"),
    CONSTANT_VALUE("constant", "constantValue"),
    JZ_LABEL("label", "JZLabel"),
    JE_CONSTANT_LABEL("label",  "JEConstantLabel"),
    VARIABLE_NAME("variable", "variableName"),
    JE_VARIABLE_LABEL("label",  "JEVariableLabel"),;

    private final String type;
    private final String nameInXml;

    InstructionArgument(String type, String nameInXML){
        this.type = type;
        this.nameInXml = nameInXML;
    }

    public String getType(){
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
