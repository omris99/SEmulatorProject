package logic.execution;

import logic.model.argument.variable.Variable;

import java.util.Map;

public interface ProgramExecutor {
    Map<Variable, Long> run(int degree, Long... input);
    Map<Variable, Long> variableState();
    int getCyclesCount();
}
