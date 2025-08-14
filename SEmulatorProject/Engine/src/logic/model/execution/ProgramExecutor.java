package logic.model.execution;

import logic.model.variable.Variable;

import java.util.Map;

public interface ProgramExecutor {
    long run(Long... input);
    Map<Variable, Long> variableState();
}
