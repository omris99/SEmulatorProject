package logic.execution;

import logic.model.argument.variable.Variable;

import java.util.Map;

public class ExecutionRecord {
    int degree;
    Map<Variable, Long> inputVariables;
    long yValue;
    int totalCycles;

    public ExecutionRecord(int degree, Map<Variable, Long> inputVariables, long yValue, int totalCycles) {
        this.degree = degree;
        this.inputVariables = inputVariables;
        this.yValue = yValue;
        this.totalCycles = totalCycles;
    }
}
