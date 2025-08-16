package logic.model.argument.variable;

import logic.model.argument.Argument;

public interface Variable extends Argument {
    VariableType getType();
    String getRepresentation();
    int getNumber();
    Variable RESULT = new VariableImpl(VariableType.RESULT, 0);
}
