package logic.model.argument.variable;

import logic.model.argument.Argument;

import java.io.Serializable;

public interface Variable extends Argument, Serializable {
    VariableType getType();
    String getRepresentation();
    int getNumber();
    Variable RESULT = new VariableImpl(VariableType.RESULT, 0);
}
