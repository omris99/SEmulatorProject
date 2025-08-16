package logic.execution;

import logic.model.argument.variable.Variable;

import java.util.Map;

public interface ProgramExecutor {
    long run(Long... input);
    Map<Variable, Long> variableState();
}
