package serverengine.logic.model.argument.variable;

import types.errortypes.ArgumentErrorType;
import exceptions.InvalidArgumentException;
import serverengine.logic.model.argument.Argument;

import java.util.Objects;

public class VariableImpl implements Variable, Argument {

    private final VariableType type;
    private final int number;

    @Override
    public int getIndex() {
        return number;
    }

    private VariableImpl parse(String stringVariable)
    {
        if (stringVariable == null || stringVariable.isEmpty()) {
            throw new IllegalArgumentException("Variable string cannot be null or empty");
        }

        String varKind = stringVariable.substring(0, 1);
        VariableType varType = stringVarTypeToVariableType(varKind);
        if(varType == null){
            throw new InvalidArgumentException(stringVariable, ArgumentErrorType.VARIABLE_PREFIX_INVALID);
        }

        if (varType.equals(VariableType.RESULT)) {
            if (stringVariable.length() != 1) {
                throw new InvalidArgumentException(stringVariable, ArgumentErrorType.Y_VARIABLE_HAS_INDEX);
            }

            return new VariableImpl(VariableType.RESULT, 0);
        }

        if (stringVariable.length() < 2) {
            throw new InvalidArgumentException(stringVariable, ArgumentErrorType.VARIABLE_INDEX_MISSING);
        }

        int varIndex;

        try {
            varIndex = Integer.parseInt(stringVariable.substring(1));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException(stringVariable, ArgumentErrorType.VARIABLE_INDEX_CANT_PARSE_TO_NUMBER);
        }

        if (!isStringVariableIndexIsPositive(varIndex)) {
            throw new InvalidArgumentException(stringVariable, ArgumentErrorType.VARIABLE_INDEX_IS_NEGATIVE);
        }

        return new VariableImpl(varType, varIndex);
    }

    public VariableImpl(VariableType type, int number) {
        this.type = type;
        this.number = number;
    }

    public VariableImpl(String stringVariable) {
        VariableImpl parsedVar = parse(stringVariable);
        this.type = parsedVar.type;
        this.number = parsedVar.number;
    }

    @Override
    public VariableType getType() {
        return type;
    }

    @Override
    public String getRepresentation() {
        return type.getVariableRepresentation(number);
    }

    public static VariableType stringVarTypeToVariableType(String type) {
        VariableType variableType;
        switch (type.toLowerCase()) {
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
                variableType = null;
                break;
        }

        return variableType;
    }

    private static boolean isStringVariableIndexIsPositive(int index) {
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