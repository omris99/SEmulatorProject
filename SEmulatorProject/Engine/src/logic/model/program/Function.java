package logic.model.program;

import dto.DTO;
import logic.model.argument.Argument;
import logic.model.argument.ArgumentType;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.label.LabelImpl;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.instruction.Instruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionWithArguments;
import logic.model.instruction.Instructions;
import logic.model.mappers.InstructionMapper;

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

    public QuotedFunction quote(int maxWorkVariableIndex, int maxLabelIndex){
        AtomicInteger maxWorkVariableIndexAtomic = new AtomicInteger(maxWorkVariableIndex);
        AtomicInteger maxWorkVariableLabelAtomic = new AtomicInteger(maxLabelIndex);

        List<Instruction> quotedFunctionInstructions = new LinkedList<>();

        Map<InstructionArgument, Argument> instructionArgumentsToNewVariables = null;
        Map<Variable, Variable> FunctionAllVariablesToFreeWorkVariablesMap = mapFunctionAllVariablesToFreeWorkVariables(maxWorkVariableIndexAtomic);
        Map<Label, Label> FunctionLabelsToFreeLabels = mapFunctionAllLabelsToFreeLabels(maxWorkVariableLabelAtomic);

        for (Instruction instruction : getInstructions()) {
            if (instruction instanceof InstructionWithArguments) {
                instructionArgumentsToNewVariables = new HashMap<>();
                Map<InstructionArgument, Argument> instructionOriginalArguments = ((InstructionWithArguments) instruction).getArguments();
                for (InstructionArgument argumentKey : instructionOriginalArguments.keySet()) {
                    if (argumentKey.getType().equals(ArgumentType.VARIABLE)) {
                        instructionArgumentsToNewVariables.put(argumentKey, FunctionAllVariablesToFreeWorkVariablesMap.get(instructionOriginalArguments.get(argumentKey)));
                    } else if (argumentKey.getType().equals(ArgumentType.LABEL)) {
                        instructionArgumentsToNewVariables.put(argumentKey, FunctionLabelsToFreeLabels.get(instructionOriginalArguments.get(argumentKey)));
                    } else {
                        instructionArgumentsToNewVariables.put(argumentKey, instructionOriginalArguments.get(argumentKey));
                    }
                }
            }

            quotedFunctionInstructions.add(InstructionMapper.createInstruction(
                    instruction.getName(),
                    FunctionAllVariablesToFreeWorkVariablesMap.get(instruction.getVariable()),
                    FunctionLabelsToFreeLabels.get(instruction.getLabel()) != null ? FunctionLabelsToFreeLabels.get(instruction.getLabel()) : FixedLabel.EMPTY,
                    instructionArgumentsToNewVariables
            ));
        }

        return new QuotedFunction(quotedFunctionInstructions, FunctionAllVariablesToFreeWorkVariablesMap, FunctionLabelsToFreeLabels);
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
}
