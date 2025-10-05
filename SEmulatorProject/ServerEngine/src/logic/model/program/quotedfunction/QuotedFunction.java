package logic.model.program.quotedfunction;

import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.Instruction;

import java.util.List;
import java.util.Map;

public class QuotedFunction {
    List<Instruction> quotedFunctionInstructions;
    Map<Variable, Variable> originalVariablesToFreeWorkVariablesMap;
    Map<Label, Label> originalLabelsToFreeLabels;

    public QuotedFunction(List<Instruction> quotedFunctionInstructions, Map<Variable, Variable> originalVariablesToFreeWorkVariablesMap, Map<Label, Label> originalLabelsToFreeLabels) {
        this.quotedFunctionInstructions = quotedFunctionInstructions;
        this.originalVariablesToFreeWorkVariablesMap = originalVariablesToFreeWorkVariablesMap;
        this.originalLabelsToFreeLabels = originalLabelsToFreeLabels;
    }

    public List<Instruction> getQuotedFunctionInstructions() {
        return quotedFunctionInstructions;
    }

    public Map<Variable, Variable> getOriginalVariablesToFreeWorkVariablesMap() {
        return originalVariablesToFreeWorkVariablesMap;
    }

    public Map<Label, Label> getOriginalLabelsToFreeLabels() {
        return originalLabelsToFreeLabels;
    }

}
