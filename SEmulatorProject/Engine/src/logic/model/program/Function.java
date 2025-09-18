package logic.model.program;

import dto.DTO;
import dto.RunResultsDTO;
import logic.exceptions.ArgumentErrorType;
import logic.exceptions.InvalidArgumentException;
import logic.exceptions.NumberNotInRangeException;
import logic.execution.ProgramExecutor;
import logic.execution.ProgramExecutorImpl;
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
import logic.model.functionsrepo.FunctionsRepo;
import logic.model.instruction.Instruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionWithArguments;
import logic.model.instruction.Instructions;
import logic.model.instruction.basic.NeutralInstruction;
import logic.model.instruction.synthetic.AssignmentInstruction;
import logic.model.instruction.synthetic.QuoteInstruction;
import logic.model.mappers.InstructionMapper;
import logic.utils.Utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Function implements Program, Argument {
    private final String name;
    private final String userString;
    private final Instructions instructions;
    private final List<Function> functions;


    public Function(String name, String userString) {
        this.name = name;
        this.userString = userString;
        this.instructions = new Instructions();
        this.functions = new LinkedList<>();
    }


    public QuotedFunction quote(int maxWorkVariableIndex, int maxLabelIndex, CommaSeperatedArguments functionArguments, Variable contextVariable, Label instructionLabel) {
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
                    switch (argumentKey.getType()) {
                        case VARIABLE:
                            instructionArgumentsToNewVariables.put(argumentKey, functionAllVariablesToFreeWorkVariablesMap.get(instructionOriginalArguments.get(argumentKey)));
                            break;
                        case LABEL:
                            instructionArgumentsToNewVariables.put(argumentKey, FunctionLabelsToFreeLabels.get(instructionOriginalArguments.get(argumentKey)));
                            break;
                        case COMMA_SEPERATED_ARGUMENTS:
                            CommaSeperatedArguments instructionArguments = (CommaSeperatedArguments) instructionOriginalArguments.get(argumentKey);
                            CommaSeperatedArguments changedInstructionArguments = instructionArguments.changeInputsToActualVariables(functionAllVariablesToFreeWorkVariablesMap);
                            instructionArgumentsToNewVariables.put(argumentKey, changedInstructionArguments);
                        case CONSTANT:
                            instructionArgumentsToNewVariables.put(argumentKey, instructionOriginalArguments.get(argumentKey));
                            break;
                        case NAME:
                            String functionName = instructionOriginalArguments.get(argumentKey).getRepresentation();
                            if(FunctionsRepo.getInstance().isFunctionExist(functionName)){
                                instructionArgumentsToNewVariables.put(argumentKey, instructionOriginalArguments.get(argumentKey));
                            }
                            else {
                                throw new InvalidArgumentException(functionName, ArgumentErrorType.FUNCTION_NOT_FOUND);
                            }
                            break;
                        default:
                            throw new InvalidArgumentException(instructionOriginalArguments.get(argumentKey).getRepresentation(), ArgumentErrorType.UNKNOWN_ARGUMENT);
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

        List<Instruction> argumentsAssignmentToFreeWorkVariablesInstructions = new LinkedList<>();
        List<String> contextFunctionArguments = functionArguments.extractArguments();
        int inputIndex = 1;
        for(String argument : contextFunctionArguments){
            if (VariableImpl.stringVarTypeToVariableType(argument.substring(0,1)) != null) {
                argumentsAssignmentToFreeWorkVariablesInstructions.add(new AssignmentInstruction(functionAllVariablesToFreeWorkVariablesMap.get(new VariableImpl(VariableType.INPUT, inputIndex)), new VariableImpl(argument)));
            }else if(argument.startsWith("(") && argument.endsWith(")")) {
                CommaSeperatedArguments nestedArguments = new CommaSeperatedArguments(argument.substring(1, argument.length() - 1));
                List<String> functionCallargumentsList = nestedArguments.extractArguments();
                functionCallargumentsList.removeFirst();
                String commaSeperatedFunctionCallArguments = String.join(",", functionCallargumentsList);
                argumentsAssignmentToFreeWorkVariablesInstructions.add(new QuoteInstruction(functionAllVariablesToFreeWorkVariablesMap.get(new VariableImpl(VariableType.INPUT, inputIndex)), new NameArgument(nestedArguments.extractArguments().getFirst()), new CommaSeperatedArguments(commaSeperatedFunctionCallArguments)));
            }

            inputIndex++;
        }

        argumentsAssignmentToFreeWorkVariablesInstructions.addFirst(new NeutralInstruction(Variable.RESULT, instructionLabel));

        Instruction returnResultToOriginalVariableInstruction = new AssignmentInstruction(
                contextVariable,
                functionAllVariablesToFreeWorkVariablesMap.get(Variable.RESULT),
                FunctionLabelsToFreeLabels.getOrDefault(FixedLabel.EXIT, FixedLabel.EMPTY));

        return new QuotedFunction(
                argumentsAssignmentToFreeWorkVariablesInstructions,
                quotedFunctionInstructions,
                returnResultToOriginalVariableInstruction,
                functionAllVariablesToFreeWorkVariablesMap,
                FunctionLabelsToFreeLabels);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions.getInstructionsList();
    }

    @Override
    public Program getExpandedProgram(int degree) {
        return null;
    }

    @Override
    public Set<Label> getAllInstructionsLabels() {
        return instructions.getLabels();
    }

    @Override
    public Set<Variable> getAllInstructionsInputs() {
        return instructions.getInputs();
    }

    @Override
    public Set<Variable> getAllInstructionsWorkVariables() {
        return instructions.getWorkVariables();
    }

    @Override
    public Label validate() {
        return null;
    }

    @Override
    public int getMaximalDegree() {
        return 0;
    }

    @Override
    public DTO createDTO() {
        return null;
    }

    @Override
    public int getDegree() {
        return 0;
    }

    @Override
    public String getRepresentation() {
        return userString;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    public List<Function> getFunctions() {
        return functions;
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

    @Override
    public Argument parse(String stringArgument) {
        return null;
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

    public int getCycles(){
        int totalCycles = 0;
//        for(Instruction instruction : getInstructions()){
//                totalCycles += instruction.getCycles();
//        }

        return totalCycles;
    }
}
