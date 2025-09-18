package logic.model.instruction.synthetic;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.execution.ExecutionContextImpl;
import logic.execution.ProgramExecutor;
import logic.execution.ProgramExecutorImpl;
import logic.model.argument.Argument;
import logic.model.argument.NameArgument;
import logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.functionsrepo.FunctionsRepo;
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
        Function functionToExecute = FunctionsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());
//        List<String> argumentList = ((CommaSeperatedArguments) arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)).extractArguments();
//        int inputVariableIndex = 1;
//        ProgramExecutor executor = new ProgramExecutorImpl(functionToExecute);
//        Map<Variable, Long> inputVariablesMap = new HashMap<>();
//        for(String argument : argumentList){
//            if(argument.startsWith("(") && argument.endsWith(")")) {
//                CommaSeperatedArguments nestedArguments = new CommaSeperatedArguments(argument.substring(1, argument.length() - 1));
//                List<String> functionCallargumentsList = nestedArguments.extractArguments();
//                functionCallargumentsList.removeFirst();
//                String commaSeperatedFunctionCallArguments = String.join(",", functionCallargumentsList);
//                new QuoteInstruction(new VariableImpl(VariableType.INPUT, inputVariableIndex), new NameArgument(nestedArguments.extractArguments().getFirst()), new CommaSeperatedArguments(commaSeperatedFunctionCallArguments)).execute(context);
//            }
//            else{
//                inputVariablesMap.put(new VariableImpl(VariableType.INPUT, inputVariableIndex), 0L);
//            }
//            inputVariableIndex++;
//
//        }
//        Long functionRunResult = executor.run(inputVariablesMap).get(Variable.RESULT);
        context.updateVariable(getVariable(), functionToExecute.run(((CommaSeperatedArguments) arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)), context.getVariablesStatus()));
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
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
//        int maxInputVariableIndex = Utils.getMaxGeneralVariableIndex(programInputVariables);
        Function contextFunction = FunctionsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());

        QuotedFunction quotedFunction = contextFunction.quote(maxWorkVariableIndex, maxLabelIndex);
        List<Instruction> expandedInstructions = quotedFunction.getQuotedFunctionInstructions();

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
        expandedInstructions.addFirst(new NeutralInstruction(Variable.RESULT, instructionLabel));

        expandedInstructions.add(new AssignmentInstruction(
                getVariable(),
                quotedFunction.getOriginalVariablesToFreeWorkVariablesMap().get(Variable.RESULT),
                quotedFunction.getOriginalLabelsToFreeLabels().getOrDefault(FixedLabel.EXIT, FixedLabel.EMPTY)));

        return expandedInstructions;
    }


    @Override
    public Instruction clone() {
        QuoteInstruction copy = (QuoteInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }

    @Override
    public int getCycles(){
        int totalCycles = super.getCycles();
        Function contextFunction = FunctionsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());
        if(contextFunction != null) {
            totalCycles += contextFunction.getTotalCycles();
        }
        return totalCycles;
    }
}
