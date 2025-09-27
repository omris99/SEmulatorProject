package logic.model.program;

import logic.exceptions.NumberNotInRangeException;
import logic.execution.ProgramExecutor;
import logic.execution.ProgramExecutorImpl;
import logic.model.argument.Argument;
import logic.model.argument.ArgumentType;
import logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.label.LabelImpl;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.functionsrepo.FunctionsRepo;
import logic.model.instruction.Instruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionWithArguments;
import logic.model.mappers.InstructionMapper;
import logic.model.program.quotedfunction.QuotedFunction;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Function extends AbstractProgram implements Argument {
    private final String userString;

    public Function(String name, String userString) {
        super(name);
        this.userString = userString;
    }

    public QuotedFunction quote(int maxWorkVariableIndex, int maxLabelIndex){
        AtomicInteger maxWorkVariableIndexAtomic = new AtomicInteger(maxWorkVariableIndex);
        AtomicInteger maxWorkVariableLabelAtomic = new AtomicInteger(maxLabelIndex);

        List<Instruction> quotedFunctionInstructions = new LinkedList<>();

        Map<InstructionArgument, Argument> instructionArgumentsToNewVariables = null;
        Map<Variable, Variable> functionAllVariablesToFreeWorkVariablesMap = mapFunctionAllVariablesToFreeWorkVariables(maxWorkVariableIndexAtomic);
        Map<Label, Label> FunctionLabelsToFreeLabels = mapFunctionAllLabelsToFreeLabels(maxWorkVariableLabelAtomic);

        for (Instruction instruction : getInstructions()) {
            if (instruction instanceof InstructionWithArguments) {
                instructionArgumentsToNewVariables = new HashMap<>();
                Map<InstructionArgument, Argument> instructionOriginalArguments = ((InstructionWithArguments) instruction).getArguments();
                for (InstructionArgument argumentKey : instructionOriginalArguments.keySet()) {
                    if (argumentKey.getType().equals(ArgumentType.VARIABLE)) {
                        instructionArgumentsToNewVariables.put(argumentKey, functionAllVariablesToFreeWorkVariablesMap.get(instructionOriginalArguments.get(argumentKey)));
                    } else if (argumentKey.getType().equals(ArgumentType.LABEL)) {
                        instructionArgumentsToNewVariables.put(argumentKey, FunctionLabelsToFreeLabels.get(instructionOriginalArguments.get(argumentKey)));
                    } else if (argumentKey.getType().equals(ArgumentType.COMMA_SEPERATED_ARGUMENTS)) {
                        CommaSeperatedArguments instructionArguments = (CommaSeperatedArguments) instructionOriginalArguments.get(argumentKey);
                        CommaSeperatedArguments changedInstructionArguments = instructionArguments.changeInputsToActualVariables(functionAllVariablesToFreeWorkVariablesMap);
                        instructionArgumentsToNewVariables.put(argumentKey, changedInstructionArguments);
                    }
                    else{
                        instructionArgumentsToNewVariables.put(argumentKey, instructionOriginalArguments.get(argumentKey));
                    }
                }
            }

            quotedFunctionInstructions.add(InstructionMapper.createInstruction(
                    instruction.getName(),
                    functionAllVariablesToFreeWorkVariablesMap.get(instruction.getVariable()),
                    FunctionLabelsToFreeLabels.get(instruction.getLabel()) != null ? FunctionLabelsToFreeLabels.get(instruction.getLabel()) : FixedLabel.EMPTY,
                    instructionArgumentsToNewVariables
            ));
        }

        return new QuotedFunction(quotedFunctionInstructions, functionAllVariablesToFreeWorkVariablesMap, FunctionLabelsToFreeLabels);
    }

    @Override
    public Program getExpandedProgram(int degree) {

        if(degree > getMaximalDegree()){
            throw new NumberNotInRangeException(degree);
        }
        else if(degree == 0){
            return this;
        }
        else {
            Function expandedProgram = new Function(name, userString);
            for (Instruction instruction : instructions.getInstructionsList()) {
                expandedProgram.addInstruction(instruction.clone());
            }

            for(int i = 0; i < degree; i++){
                expandedProgram.instructions.expand();
            }

            return expandedProgram;
        }
    }

    @Override
    public Label validate() {
        return null;
    }

    @Override
    public String getRepresentation() {
        return userString;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    private Map<Variable, Variable> mapFunctionAllVariablesToFreeWorkVariables(AtomicInteger maxWorkVariableIndex) {
        Map<Variable, Variable> functionAllVariablesToFreeWorkVariablesMap = new HashMap<>();

        for (Variable functionInputVariable : getAllInstructionsInputs()) {
            functionAllVariablesToFreeWorkVariablesMap.put(functionInputVariable, new VariableImpl(VariableType.WORK, maxWorkVariableIndex.incrementAndGet()));
        }

        functionAllVariablesToFreeWorkVariablesMap.put(Variable.RESULT, new VariableImpl(VariableType.WORK, maxWorkVariableIndex.incrementAndGet()));

        for (Variable functionWorkVariable : getAllInstructionsWorkVariables()) {
            functionAllVariablesToFreeWorkVariablesMap.put(functionWorkVariable, new VariableImpl(VariableType.WORK, maxWorkVariableIndex.incrementAndGet()));
        }

        return functionAllVariablesToFreeWorkVariablesMap;
    }

    private Map<Label, Label> mapFunctionAllLabelsToFreeLabels(AtomicInteger maxLabelIndex) {
        Map<Label, Label> functionLabelsToFreeLabelsMap = new HashMap<>();
        for (Label functionLabel : getAllInstructionsLabels()) {
            functionLabelsToFreeLabelsMap.put(functionLabel, new LabelImpl(maxLabelIndex.incrementAndGet()));
        }

        return functionLabelsToFreeLabelsMap;
    }

    public Long run(CommaSeperatedArguments arguments, Map<Variable, Long> programVariablesStatus) {
        ProgramExecutor executor = new ProgramExecutorImpl(this);
        List<String> inputs = arguments.extractArguments();
        Map<Variable, Long> functionUseInitialInputVariablesMap = new LinkedHashMap<>();
        functionUseInitialInputVariablesMap.put(Variable.RESULT, 0L);

        int inputIndex = 1;
        for (String input : inputs) {
            if(input.startsWith("(") && input.endsWith(")")) {
                CommaSeperatedArguments nestedArguments = new CommaSeperatedArguments(input.substring(1, input.length() - 1));
                List<String> functionCallargumentsList = nestedArguments.extractArguments();
                String functionName = functionCallargumentsList.removeFirst();
                String commaSeperatedFunctionCallArguments = String.join(",", functionCallargumentsList);
                Map<Variable, Long> freshContext = new HashMap<>(programVariablesStatus);
                Long nestedResult = FunctionsRepo.getInstance()
                        .getFunctionByName(functionName)
                        .run(new CommaSeperatedArguments(commaSeperatedFunctionCallArguments), freshContext);

                functionUseInitialInputVariablesMap.put(new VariableImpl(VariableType.INPUT, inputIndex), nestedResult);
            }
            else{
                functionUseInitialInputVariablesMap.put(new VariableImpl(VariableType.INPUT, inputIndex), programVariablesStatus.get(new VariableImpl(input)));
            }
            inputIndex++;
        }

        return executor.run(new LinkedHashMap<>(functionUseInitialInputVariablesMap)).get(Variable.RESULT);
    }

    public int getTotalCycles(){
        return instructions.getTotalCycles();
    }
}
