package logic.utils;

import logic.model.argument.Argument;
import logic.model.argument.ArgumentType;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableType;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Utils {
    public static Map<Variable, Long> extractVariablesTypesFromMap(Map<Variable, Long> variables, VariableType variableType) {
        Map<Variable, Long> result = new LinkedHashMap<>();

        for(Map.Entry<Variable, Long> entry : variables.entrySet()){
            if(entry.getKey().getType().equals(variableType)){
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public static Map<Variable, Long> createInputVariablesMap(Set<Variable> variables, Long... inputs) {
        Map<Variable, Long> inputVariablesMap = new LinkedHashMap<>();
        int i = 0;

        for (Variable inputVariable : variables) {
            long value = (i < inputs.length) ? inputs[i] : 0L;
            inputVariablesMap.put(inputVariable, value);
            i++;
        }

        return inputVariablesMap;
    }

    public static int getMaxLabelIndex(Set<Label> instructionsLabels) {
        return instructionsLabels.stream().map(Argument::getIndex).max(Comparator.naturalOrder()).orElse(0);
    }

    public static int getMaxGeneralVariableIndex(Set<Variable> instructionsVariables) {
        return instructionsVariables.stream().map(Argument::getIndex).max(Comparator.naturalOrder()).orElse(0);
    }

}
