package logic.model.instruction.synthetic;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.model.argument.Argument;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.label.LabelImpl;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.instruction.*;
import logic.model.instruction.basic.JumpNotZeroInstruction;
import logic.model.instruction.basic.NeutralInstruction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QuoteInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Map<InstructionArgument, Argument> arguments;

    public QuoteInstruction(Variable variable, Argument functionName, Argument functionArguments) {
        this(variable, functionName, functionArguments, FixedLabel.EMPTY);
    }

    public QuoteInstruction(Variable variable, Argument function, Argument functionArguments, Label label) {
        super(InstructionData.QUOTE, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.FUNCTION_NAME, function);
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
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel) {
        List<Instruction> expandedInstructions = new LinkedList<>();
        expandedInstructions.add(new NeutralInstruction(Variable.RESULT, instructionLabel));

//        Label freeLabel = new LabelImpl(maxLabelIndex + 1);
//        Variable workVariable = new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1);
//
//        expandedInstructions.add(new JumpNotZeroInstruction(getVariable(), freeLabel, instructionLabel));
//        expandedInstructions.add(new GoToLabelInstruction(workVariable, arguments.get(InstructionArgument.JZ_LABEL)));
//        expandedInstructions.add(new NeutralInstruction(Variable.RESULT, freeLabel));

        return expandedInstructions;
    }


    @Override
    public Instruction clone() {
        QuoteInstruction copy = (QuoteInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }
}