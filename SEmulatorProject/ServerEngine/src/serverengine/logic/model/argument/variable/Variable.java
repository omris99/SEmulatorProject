package serverengine.logic.model.argument.variable;

import serverengine.logic.model.argument.Argument;

import java.io.Serializable;

public interface Variable extends Argument, Serializable {
    VariableType getType();
    String getRepresentation();
    int getNumber();
    Variable RESULT = new VariableImpl(VariableType.RESULT, 0);
}
