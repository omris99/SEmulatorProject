package clientserverdto;


import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RunResultsDTO implements DTO {
    private final int degree;
    private final Long yValue;
    private final Map<String, Long> inputVariablesInitialValues;
    private final Map<String, Long> inputVariablesValueResult;
    private final Map<String, Long> workVariablesValues;
    private final int totalCyclesCount;
    private final String architecture;
    private boolean isFinished = false;

    public RunResultsDTO(int degree, Long yValue, Map<String, Long> inputVariablesInitialValues, Map<String, Long> inputVariablesValueResult, Map<String, Long> workVariablesValues, int totalCyclesCount, String architecture) {
        this(degree, yValue, inputVariablesInitialValues, inputVariablesValueResult, workVariablesValues, totalCyclesCount, architecture, false);
    }

    public RunResultsDTO(int degree,
                         Long yValue,
                         Map<String, Long> inputVariablesInitialValues,
                         Map<String, Long> inputVariablesValueResult,
                         Map<String, Long> workVariablesValues,
                         int totalCyclesCount,
                         String architecture,
                         boolean isFinished) {
        this.degree = degree;
        this.yValue = yValue;
        this.inputVariablesInitialValues = inputVariablesInitialValues;
        this.inputVariablesValueResult = inputVariablesValueResult;
        this.workVariablesValues = workVariablesValues;
        this.totalCyclesCount = totalCyclesCount;
        this.isFinished = isFinished;
        this.architecture = architecture;
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
}
