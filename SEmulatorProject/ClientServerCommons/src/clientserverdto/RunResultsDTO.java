package clientserverdto;


import types.modeltypes.ArchitectureType;

import java.util.Map;

public class RunResultsDTO implements DTO {
    private final int degree;
    private final Long yValue;
    private final Map<String, Long> inputVariablesInitialValues;
    private final Map<String, Long> inputVariablesValueResult;
    private final Map<String, Long> workVariablesValues;
    private final int totalCyclesCount;
    private final String architecture;
    private final Map<ArchitectureType, Long> performedInstructionsCountByArchitecture;
    private boolean isFinished = false;

    public RunResultsDTO(int degree,
                         Long yValue,
                         Map<String, Long> inputVariablesInitialValues,
                         Map<String, Long> inputVariablesValueResult,
                         Map<String, Long> workVariablesValues,
                         int totalCyclesCount,
                         String architecture,
                         Map<ArchitectureType, Long> PerformedInstructionsCountByArchitecture,
                         boolean isFinished) {
        this.degree = degree;
        this.yValue = yValue;
        this.inputVariablesInitialValues = inputVariablesInitialValues;
        this.inputVariablesValueResult = inputVariablesValueResult;
        this.workVariablesValues = workVariablesValues;
        this.totalCyclesCount = totalCyclesCount;
        this.architecture = architecture;
        this.performedInstructionsCountByArchitecture = PerformedInstructionsCountByArchitecture;
        this.isFinished = isFinished;
    }


    public Long getYValue() {
        return yValue;
    }

    public Map<String, Long> getInputVariablesInitialValues() {
        return inputVariablesInitialValues;
    }

    public Map<String, Long> getInputVariablesValueResult() {
        return inputVariablesValueResult;
    }

    public Map<String, Long> getWorkVariablesValues() {
        return workVariablesValues;
    }

    public int getTotalCyclesCount() {
        return totalCyclesCount;
    }

    public int getDegree() {
        return degree;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public String getArchitecture() {
        return architecture;
    }

    public Map<ArchitectureType, Long> getPerformedInstructionsCountByArchitecture() {
        return performedInstructionsCountByArchitecture;
    }
}
