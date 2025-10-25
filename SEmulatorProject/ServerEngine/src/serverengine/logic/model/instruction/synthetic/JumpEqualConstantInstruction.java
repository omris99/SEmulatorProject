package serverengine.logic.model.instruction.synthetic;

import clientserverdto.InstructionDTO;
import serverengine.logic.execution.ExecutionContext;
import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.constant.Constant;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.label.LabelImpl;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.argument.variable.VariableImpl;
import serverengine.logic.model.argument.variable.VariableType;
import serverengine.logic.model.instruction.*;
import serverengine.logic.model.instruction.basic.DecreaseInstruction;
import serverengine.logic.model.instruction.basic.JumpNotZeroInstruction;
import serverengine.logic.model.instruction.basic.NeutralInstruction;

import java.util.*;

public class JumpEqualConstantInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Map<InstructionArgument, Argument> arguments;

    public JumpEqualConstantInstruction(Variable variable, Argument jeConstantLabel, Argument constantValue) {
        this(variable, jeConstantLabel, constantValue, FixedLabel.EMPTY);
    }

    public JumpEqualConstantInstruction(Variable variable, Argument jeConstantLabel, Argument constantValue, Label label) {
        super(InstructionData.JUMP_EQUAL_CONSTANT, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.JE_CONSTANT_LABEL, jeConstantLabel);
        arguments.put(InstructionArgument.CONSTANT_VALUE, constantValue);
    }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());

        if (variableValue == ((Constant)arguments.get(InstructionArgument.CONSTANT_VALUE)).getValue()) {
            return (Label) arguments.get(InstructionArgument.JE_CONSTANT_LABEL);
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("IF %s = %s GOTO %s",
                getVariable().getRepresentation(), (arguments.get(InstructionArgument.CONSTANT_VALUE).getRepresentation()),
                (arguments.get(InstructionArgument.JE_CONSTANT_LABEL).getRepresentation()));

        return super.getInstructionDTO(displayFormat);
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("IF %s = %s GOTO %s",
                getVariable().getRepresentation(), (arguments.get(InstructionArgument.CONSTANT_VALUE).getRepresentation()),
                (arguments.get(InstructionArgument.JE_CONSTANT_LABEL).getRepresentation()));

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }

    @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
        List<Instruction> expandedInstructions = new LinkedList<>();
        Label freeLabel = new LabelImpl(maxLabelIndex + 1);
        int constantValue = ((Constant)arguments.get(InstructionArgument.CONSTANT_VALUE)).getValue();
        Variable workVariable1 = new VariableImpl(VariableType.WORK ,maxWorkVariableIndex + 1);
        Variable workVariable2 = new VariableImpl(VariableType.WORK ,maxWorkVariableIndex + 2);

        expandedInstructions.add(new AssignmentInstruction(workVariable1, getVariable(), instructionLabel));
        for (int i = 0; i < constantValue; i++) {
            expandedInstructions.add(new JumpZeroInstruction(workVariable1, freeLabel));
            expandedInstructions.add(new DecreaseInstruction(workVariable1));
        }

        expandedInstructions.add(new JumpNotZeroInstruction(workVariable1, freeLabel));
        expandedInstructions.add(new GoToLabelInstruction(workVariable2, arguments.get(InstructionArgument.JE_CONSTANT_LABEL)));
        expandedInstructions.add(new NeutralInstruction(Variable.RESULT, freeLabel));

        return expandedInstructions;
    }

    @Override
    public Instruction clone() {
        JumpEqualConstantInstruction copy = (JumpEqualConstantInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }
}
