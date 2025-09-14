package dto;

import logic.model.argument.variable.Variable;

import java.util.Map;

public class DebugResultsDTO extends RunResultsDTO {
    private boolean isFinished;

    public DebugResultsDTO(int degree, Long yValue, Map<Variable, Long> inputVariablesAsEntered, Map<Variable, Long> workVariablesValues, int totalCyclesCount, boolean isFinished) {
        super(degree, yValue, inputVariablesAsEntered, workVariablesValues, totalCyclesCount);
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
