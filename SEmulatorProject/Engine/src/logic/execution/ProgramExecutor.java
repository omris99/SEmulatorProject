package logic.execution;

import logic.model.argument.variable.Variable;

import java.util.Map;

public interface ProgramExecutor {
    Map<Variable, Long> run(Long... input);
    Map<Variable, Long> variableState();
    int getCyclesCount();
}
