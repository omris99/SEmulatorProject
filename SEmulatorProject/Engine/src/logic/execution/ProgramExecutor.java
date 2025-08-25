package logic.execution;

import logic.model.argument.variable.Variable;

import java.io.Serializable;
import java.util.Map;

public interface ProgramExecutor {
    Map<Variable, Long> run(Map<Variable, Long> inputVariablesMap);
    Map<Variable, Long> variableState();
    int getCyclesCount();
}
