package logic.execution;

import logic.model.argument.variable.Variable;

public interface ExecutionContext {

    long getVariableValue(Variable v);
    void updateVariable(Variable v, long value);
}