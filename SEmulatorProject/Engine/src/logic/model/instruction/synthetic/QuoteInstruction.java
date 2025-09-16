package logic.model.instruction.synthetic;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.model.argument.Argument;
import logic.model.argument.ArgumentType;
import logic.model.argument.NameArgument;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.label.LabelImpl;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.instruction.*;
import logic.model.instruction.basic.DecreaseInstruction;
import logic.model.instruction.basic.JumpNotZeroInstruction;
import logic.model.instruction.basic.NeutralInstruction;
import logic.model.mappers.InstructionMapper;
import logic.model.program.Function;
import logic.utils.Utils;

import java.util.*;

public class QuoteInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Function contextFunction;
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
    public List<Instruction> expand(Set<Label> programLabels, Set<Variable> programWorkVariables, Set<Variable> programInputVariables, Label instructionLabel){
        int maxLabelIndex = Utils.getMaxLabelIndex(programLabels);
        int maxWorkVariableIndex = Utils.getMaxGeneralVariableIndex(programWorkVariables);
        int maxInputVariableIndex = Utils.getMaxGeneralVariableIndex(programInputVariables);
        List<Instruction> expandedInstructions = new LinkedList<>();
        Map<InstructionArgument, Argument> instructionArgumentsToNewVariables = null;
        Map<Variable, Variable> FunctionAllVariablesToFreeWorkVariablesMap = new HashMap<>();

        expandedInstructions.add(new NeutralInstruction(Variable.RESULT, instructionLabel));

        for (Variable functionInputVariable : contextFunction.getAllInstructionsInputs()) {
            FunctionAllVariablesToFreeWorkVariablesMap.put(functionInputVariable, new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1));
        }

        FunctionAllVariablesToFreeWorkVariablesMap.put(Variable.RESULT, new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1));

        for(Variable functionWorkVariable : contextFunction.getAllInstructionsWorkVariables()) {
            FunctionAllVariablesToFreeWorkVariablesMap.put(functionWorkVariable, new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1));
        }

        Map<Label, Label> FunctionLabelsToFreeLabels = new HashMap<>();
        for(Label functionLabel : contextFunction.getAllInstructionsLabels()) {
            FunctionLabelsToFreeLabels.put(functionLabel, new LabelImpl(maxLabelIndex + 1));
        }

        for(Instruction instruction : contextFunction.getInstructions()) {
            if(instruction instanceof InstructionWithArguments) {
                instructionArgumentsToNewVariables = new HashMap<>();
                Map<InstructionArgument, Argument> instructionOriginalArguments = ((InstructionWithArguments) instruction).getArguments();
                for (InstructionArgument argumentKey : instructionOriginalArguments.keySet()) {
                    if(argumentKey.getType().equals(ArgumentType.VARIABLE)){
                        instructionArgumentsToNewVariables.put(argumentKey, FunctionAllVariablesToFreeWorkVariablesMap.get(instructionOriginalArguments.get(argumentKey)));
                    }
                    else if(argumentKey.getType().equals(ArgumentType.LABEL)){
                        instructionArgumentsToNewVariables.put(argumentKey, FunctionLabelsToFreeLabels.get(instructionOriginalArguments.get(argumentKey)));
                    }
                    else{
                        instructionArgumentsToNewVariables.put(argumentKey, instructionOriginalArguments.get(argumentKey));
                    }
                }
            }

            expandedInstructions.add(InstructionMapper.createInstruction(
                    instruction.getName(),
                    FunctionAllVariablesToFreeWorkVariablesMap.get(instruction.getVariable()),
                    FunctionLabelsToFreeLabels.get(instruction.getLabel()) != null ? FunctionLabelsToFreeLabels.get(instruction.getLabel()) : FixedLabel.EMPTY,
                    instructionArgumentsToNewVariables
            ));
        }

        expandedInstructions.add(new AssignmentInstruction(
                getVariable(),
                FunctionAllVariablesToFreeWorkVariablesMap.get(Variable.RESULT),
                FunctionLabelsToFreeLabels.getOrDefault(FixedLabel.EXIT, FixedLabel.EMPTY)));

        return expandedInstructions;
    }


    @Override
    public Instruction clone() {
        QuoteInstruction copy = (QuoteInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }

    public void setContextFunction(Function function) {
        this.contextFunction = function;
    }
}