package logic.execution;

import logic.model.argument.variable.Variable;

import java.util.Map;
import java.util.Set;

public class ExecutionContextImpl implements ExecutionContext {
    private final Map<Variable, Long> variablesStatus;

    public ExecutionContextImpl(Map<Variable, Long> inputVariablesMap, Set<Variable> programWorkVariables) {
        variablesStatus = inputVariablesMap;

        variablesStatus.put(Variable.RESULT, 0L);

        for(Variable variable : programWorkVariables) {
            variablesStatus.put(variable, 0L);
        }
    }

    @Override
    public long getVariableValue(Variable v) {

        return variablesStatus.get(v);
    }

    @Override
    public void updateVariable(Variable v, long value) {

        variablesStatus.put(v, value);
    }

    @Override
    public Map<Variable, Long> getVariablesStatus() {
        return variablesStatus;
    }
}
