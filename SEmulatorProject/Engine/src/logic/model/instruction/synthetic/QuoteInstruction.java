package logic.model.instruction.synthetic;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.model.argument.Argument;
import logic.model.argument.ArgumentType;
import logic.model.argument.NameArgument;
import logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
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
import logic.model.program.Program;
import logic.model.program.ProgramImpl;
import logic.model.program.QuotedFunction;
import logic.utils.Utils;

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
    public List<Instruction> expand(Program program, Label instructionLabel){
        int maxLabelIndex = Utils.getMaxLabelIndex(program.getAllInstructionsLabels());
        int maxWorkVariableIndex = Utils.getMaxGeneralVariableIndex(program.getAllInstructionsWorkVariables());
//        int maxInputVariableIndex = Utils.getMaxGeneralVariableIndex(programInputVariables);
        Function contextFunction = ((ProgramImpl) program).getFunctionByName((arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation()));

        QuotedFunction quotedFunction = contextFunction.quote(maxWorkVariableIndex, maxLabelIndex);
        List<Instruction> expandedInstructions = quotedFunction.getQuotedFunctionInstructions();
        expandedInstructions.addFirst(new NeutralInstruction(Variable.RESULT, instructionLabel));


//        List<Instruction> initialInputVariablesValues = new LinkedList<>();
//        List<String> seperatedArguments = splitArguments(arguments.get(InstructionArgument.FUNCTION_ARGUMENTS).getRepresentation());
//        int inputIndex = 1;
//        for(String argument : seperatedArguments){
//            if(argument.startsWith("(")){
//                initialInputVariablesValues.add(new QuoteInstruction(FunctionAllVariablesToFreeWorkVariablesMap.get(new VariableImpl(VariableType.INPUT, inputIndex)), new NameArgument("er"), new CommaSeperatedArguments("sac")));
//            } else {
//                initialInputVariablesValues.add(new AssignmentInstruction(FunctionAllVariablesToFreeWorkVariablesMap.get(new VariableImpl(VariableType.INPUT, inputIndex)), VariableImpl.parse(argument)));
//            }
//            inputIndex++;
//        }
//        expandedInstructions.addAll(0, initialInputVariablesValues);

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

    private List<String> splitArguments(String input) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenLevel = 0;

        for (char c : input.toCharArray()) {
            if (c == '(') {
                parenLevel++;
                current.append(c);
            } else if (c == ')') {
                parenLevel--;
                current.append(c);
            } else if (c == ',' && parenLevel == 0) {
                // פסיק מחוץ לסוגריים -> סוף אלמנט
                result.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            result.add(current.toString().trim());
        }

        return result;
    }
}
