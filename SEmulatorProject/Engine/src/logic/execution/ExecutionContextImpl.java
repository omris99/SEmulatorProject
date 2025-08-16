package logic.execution;

import logic.model.argument.variable.Variable;

import java.util.*;

public class ExecutionContextImpl implements ExecutionContext {
    private Map<Variable, Long> variablesStatus;

    public ExecutionContextImpl(Set<Variable> programInputVariables, Set<Variable> programWorkVariables, Long... inputs) {
        variablesStatus = new LinkedHashMap<>();
        int i = 0;

        for (Variable inputVariable : programInputVariables) {
            long value = (i < inputs.length) ? inputs[i] : 0L;
            variablesStatus.put(inputVariable, value);
            i++;
        }

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
}
