package logic.model.argument.variable;

import logic.model.argument.Argument;

import java.util.Objects;

public class VariableImpl implements Variable, Argument {

    private final VariableType type;
    private final int number;

//    @Override
//    public String getArgumentString() {
//        return getRepresentation();
//    }

    public static VariableImpl parse(String stringVariable)
    {
        if (stringVariable == null || stringVariable.isEmpty()) {
            throw new IllegalArgumentException("Variable string cannot be null or empty");
        }

        String varKind = stringVariable.substring(0, 1);
        VariableType varType = stringVarTypeToVariableType(varKind);

        if (varType.equals(VariableType.RESULT)) {
            if (stringVariable.length() != 1) {
                throw new IllegalArgumentException("Variable of kind 'y' must not have an index");
            }

            return new VariableImpl(VariableType.RESULT, 0);
        }

        if (stringVariable.length() < 2) {
            throw new IllegalArgumentException("Variable missing index");
        }

        int varIndex;

        try {
            varIndex = Integer.parseInt(stringVariable.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid index format in variable: " + stringVariable, e);
        }

        if (!isStringVariableIndexValid(varIndex)) {
            throw new IllegalArgumentException("Invalid index");
        }

        return new VariableImpl(varType, varIndex);
    }

    public VariableImpl(VariableType type, int number) {
        this.type = type;
        this.number = number;
    }

    @Override
    public VariableType getType() {
        return type;
    }

    @Override
    public String getRepresentation() {
        return type.getVariableRepresentation(number);
    }

    private static VariableType stringVarTypeToVariableType(String type) {
        VariableType variableType;
        switch (type) {
            case "x":
                variableType = VariableType.INPUT;
                break;
            case "y":
                variableType = VariableType.RESULT;
                break;
            case "z":
                variableType = VariableType.WORK;
                break;
            default:
                throw new IllegalArgumentException("Invalid variable type: " + type);
        }
        return variableType;
//        return kind.equals("x") || kind.equals("y") || kind.equals("z");
    }

    private static boolean isStringVariableIndexValid(int index) {
        return index >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VariableImpl variable = (VariableImpl) o;
        return number == variable.number && type == variable.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, number);
    }

    @Override
    public int getNumber() {
        return number;
    }
}