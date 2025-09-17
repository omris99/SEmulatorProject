package logic.model.instruction.synthetic;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.model.argument.Argument;
import logic.model.argument.NameArgument;
import logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.instruction.*;
import logic.model.instruction.basic.NeutralInstruction;
import logic.model.program.Function;
import logic.model.program.QuotedFunction;

import java.util.*;

public class QuoteInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Map<InstructionArgument, Argument> arguments;

    public QuoteInstruction(Variable variable, Argument functionName, Argument functionArguments) {
        this(variable, functionName, functionArguments, FixedLabel.EMPTY);
    }

    public QuoteInstruction(Variable variable, Argument functionName, Argument functionArguments, Label label) {
        super(InstructionData.QUOTE, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.FUNCTION_NAME, functionName);
        arguments.put(InstructionArgument.FUNCTION_ARGUMENTS, functionArguments);
    }

    @Override
    public Label execute(ExecutionContext context) {
//        long variableValue = context.getVariableValue(getVariable());
//
//        if (variableValue == 0) {
//            return (Label) arguments.get(InstructionArgument.JZ_LABEL);
//        }

        return FixedLabel.EMPTY;
    }

    @Override
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format(String.format("%s <- (%s,%s)", getVariable().getRepresentation(),
                arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation(),
                arguments.get(InstructionArgument.FUNCTION_ARGUMENTS).getRepresentation()));

        return super.getInstructionDTO(displayFormat);
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format(String.format("%s <- (%s,%s)", getVariable().getRepresentation(),
                arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation(),
                arguments.get(InstructionArgument.FUNCTION_ARGUMENTS).getRepresentation()));

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }


    @Override
    public List<Instruction> expand(Map<String, Function> functions, int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
//        int maxInputVariableIndex = Utils.getMaxGeneralVariableIndex(programInputVariables);
        Function contextFunction = functions.get(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());

        QuotedFunction quotedFunction = contextFunction.quote(maxWorkVariableIndex, maxLabelIndex);
        List<Instruction> expandedInstructions = quotedFunction.getQuotedFunctionInstructions();
        expandedInstructions.addFirst(new NeutralInstruction(Variable.RESULT, instructionLabel));

        List<String> contextFunctionArguments = ((CommaSeperatedArguments)arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)).extractArguments();

        List<Instruction> initialInputVariablesValues = new LinkedList<>();
        int inputIndex = 1;
        for(String argument : contextFunctionArguments){
            if (VariableImpl.stringVarTypeToVariableType(argument.substring(0,1)) != null) {
                initialInputVariablesValues.add(new AssignmentInstruction(quotedFunction.getOriginalVariablesToFreeWorkVariablesMap().get(new VariableImpl(VariableType.INPUT, inputIndex)), new VariableImpl(argument)));
            }else if(argument.startsWith("(") && argument.endsWith(")")) {
                CommaSeperatedArguments nestedArguments = new CommaSeperatedArguments(argument.substring(1, argument.length() - 1));
                List<String> functionCallargumentsList = nestedArguments.extractArguments();
                functionCallargumentsList.removeFirst();
                String commaSeperatedFunctionCallArguments = String.join(",", functionCallargumentsList);
                initialInputVariablesValues.add(new QuoteInstruction(quotedFunction.getOriginalVariablesToFreeWorkVariablesMap().get(new VariableImpl(VariableType.INPUT, inputIndex)), new NameArgument(nestedArguments.extractArguments().getFirst()), new CommaSeperatedArguments(commaSeperatedFunctionCallArguments)));
            }

            inputIndex++;
        }
        expandedInstructions.addAll(0, initialInputVariablesValues);

        expandedInstructions.add(new AssignmentInstruction(
                getVariable(),
                quotedFunction.getOriginalVariablesToFreeWorkVariablesMap().get(Variable.RESULT),
                quotedFunction.getOriginalLabelsToFreeLabels().getOrDefault(FixedLabel.EXIT, FixedLabel.EMPTY)));

        return expandedInstructions;
    }

//    private Map<Variable, Variable> mapFunctionAllVariablesToFreeWorkVariables(Function contextFunction, int maxWorkVariableIndex) {
//        Map<Variable, Variable> functionAllVariablesToFreeWorkVariablesMap = new HashMap<>();
//
//        for (Variable functionInputVariable : contextFunction.getAllInstructionsInputs()) {
//            functionAllVariablesToFreeWorkVariablesMap.put(functionInputVariable, new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1));
//        }
//
//        functionAllVariablesToFreeWorkVariablesMap.put(Variable.RESULT, new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1));
//
//        for (Variable functionWorkVariable : contextFunction.getAllInstructionsWorkVariables()) {
//            functionAllVariablesToFreeWorkVariablesMap.put(functionWorkVariable, new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1));
//        }
//
//        return functionAllVariablesToFreeWorkVariablesMap;
//    }
//
//    private Map<Label, Label> mapFunctionAllLabelsToFreeLabels(Function contextFunction, int maxLabelIndex) {
//        Map<Label, Label> functionLabelsToFreeLabelsMap = new HashMap<>();
//        for (Label functionLabel : contextFunction.getAllInstructionsLabels()) {
//            functionLabelsToFreeLabelsMap.put(functionLabel, new LabelImpl(maxLabelIndex + 1));
//        }
//
//        return functionLabelsToFreeLabelsMap;
//    }


    @Override
    public Instruction clone() {
        QuoteInstruction copy = (QuoteInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }
}
