package dto;

import logic.model.argument.variable.Variable;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RunResultsDTO implements DTO{
    private final int degree;
    private final Long yValue;
    private final Map<String, Long> inputVariablesAsEntered;
    private final Map<String, Long> workVariablesValues;
    private final int totalCyclesCount;
    private boolean isFinished = false;

    public RunResultsDTO(int degree, Long yValue, Map<Variable, Long> inputVariablesAsEntered, Map<Variable, Long> workVariablesValues, int totalCyclesCount) {
        this(degree, yValue, inputVariablesAsEntered, workVariablesValues, totalCyclesCount, false);
    }

    public RunResultsDTO(int degree, Long yValue, Map<Variable, Long> inputVariablesAsEntered, Map<Variable, Long> workVariablesValues, int totalCyclesCount, boolean isFinished) {
        this.degree = degree;
        this.yValue = yValue;
        this.inputVariablesAsEntered = convertKeyToStringAndSortVariablesMap(inputVariablesAsEntered);
        this.workVariablesValues = convertKeyToStringAndSortVariablesMap(workVariablesValues);
        this.totalCyclesCount = totalCyclesCount;
        this.isFinished = isFinished;
    }


        public Long getYValue() {
        return yValue;
    }

    public Map<String, Long> getInputVariablesAsEntered() {
        return inputVariablesAsEntered;
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

    private Map<String, Long> convertKeyToStringAndSortVariablesMap(Map<Variable, Long> variablesMap) {
        return variablesMap.entrySet().stream()
                .sorted(Comparator.comparingInt(variable -> variable.getKey().getNumber()))
                .collect(Collectors.toMap(
                        e -> e.getKey().getRepresentation(),
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public boolean isFinished(){
        return isFinished;
    }
}
