package logic.utils;

import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableType;

import java.util.LinkedHashMap;
import java.util.Map;

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
}
