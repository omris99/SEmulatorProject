package logic.model.program;

import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.Instruction;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QuotedFunction {
    List<Instruction> argumentsAssignmentToFreeWorkVariablesInstructions;
    List<Instruction> quotedFunctionInstructions;
    Instruction returnResultToOriginalVariableInstruction;
    Map<Variable, Variable> originalVariablesToFreeWorkVariablesMap;
    Map<Label, Label> originalLabelsToFreeLabels;

    public QuotedFunction(List<Instruction> argumentsAssignmentToFreeWorkVariablesInstructions,
                          List<Instruction> quotedFunctionInstructions,
                          Instruction returnResultToOriginalVariableInstruction,
                          Map<Variable, Variable> originalVariablesToFreeWorkVariablesMap,
                          Map<Label, Label> originalLabelsToFreeLabels) {
        this.argumentsAssignmentToFreeWorkVariablesInstructions = argumentsAssignmentToFreeWorkVariablesInstructions;
        this.quotedFunctionInstructions = quotedFunctionInstructions;
        this.returnResultToOriginalVariableInstruction = returnResultToOriginalVariableInstruction;
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

    public List<Instruction> getFullQuotedFunctionInstructions() {
        List<Instruction> fullInstructions = new LinkedList<>(argumentsAssignmentToFreeWorkVariablesInstructions);
        fullInstructions.addAll(quotedFunctionInstructions);
        fullInstructions.add(returnResultToOriginalVariableInstruction);
        return fullInstructions;
    }

}
