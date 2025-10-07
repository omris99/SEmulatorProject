package serverengine.logic.execution;

import serverengine.logic.model.argument.variable.Variable;

import java.util.Map;

public interface ProgramExecutor {
    Map<Variable, Long> run(Map<Variable, Long> inputVariablesMap);
    int getCyclesCount();
    int getCurrentSessionCyclesCount();
}
