package logic.model.argument;

import logic.model.argument.constant.Constant;
import logic.model.argument.variable.VariableImpl;
import logic.model.program.Function;

import java.io.Serializable;

public interface Argument extends Serializable {
    String getRepresentation();
    int getIndex();
}
