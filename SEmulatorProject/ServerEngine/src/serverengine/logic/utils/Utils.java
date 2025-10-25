package serverengine.logic.utils;

import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.argument.variable.VariableType;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {
    public static Map<String, Long> extractVariablesTypesAsStringsFromMap(Map<Variable, Long> variables, VariableType variableType) {
        Map<Variable, Long> result = new LinkedHashMap<>();

        for(Map.Entry<Variable, Long> entry : variables.entrySet()){
            if(entry.getKey().getType().equals(variableType)){
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return convertKeyToStringAndSortVariablesMap(result);
    }

    public static Map<String, Long> convertKeyToStringAndSortVariablesMap(Map<Variable, Long> variablesMap) {
        return variablesMap.entrySet().stream()
                .sorted(Comparator.comparingInt(variable -> variable.getKey().getNumber()))
                .collect(Collectors.toMap(
                        e -> e.getKey().getRepresentation(),
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}
