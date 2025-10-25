package serverengine.logic.execution;

import serverengine.logic.model.argument.variable.Variable;

import java.util.Map;

public interface ExecutionContext{

    long getVariableValue(Variable v);
    void updateVariable(Variable v, long value);
    Map<Variable, Long> getVariablesStatus();
    ExecutionContext copy();
}