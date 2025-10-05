package serverengine.logic.model.instruction.synthetic;

import clientserverdto.InstructionDTO;
import serverengine.logic.execution.ExecutionContext;
import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.constant.Constant;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.instruction.*;
import serverengine.logic.model.instruction.basic.IncreaseInstruction;

import java.util.*;

public class ConstantAssignmentInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Map<InstructionArgument, Argument> arguments;

    public ConstantAssignmentInstruction(Variable variable, Argument constantValue) {
        this(variable, constantValue, FixedLabel.EMPTY);
    }

    public ConstantAssignmentInstruction(Variable variable, Argument constantValue, Label label) {
        super(InstructionData.CONSTANT_ASSIGNMENT, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.CONSTANT_VALUE, constantValue);
    }

    @Override
    public Label execute(ExecutionContext context) {
        context.updateVariable(getVariable(),((Constant)arguments.get(InstructionArgument.CONSTANT_VALUE)).getValue());

        return FixedLabel.EMPTY;
    }

    @Override
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("%s <- %s", getVariable().getRepresentation(), arguments.get(InstructionArgument.CONSTANT_VALUE).getRepresentation());

        return super.getInstructionDTO(displayFormat);
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("%s <- %s", getVariable().getRepresentation(), arguments.get(InstructionArgument.CONSTANT_VALUE).getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }

    @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
        List<Instruction> expandedInstructions = new LinkedList<>();
        int constantValue = ((Constant)arguments.get(InstructionArgument.CONSTANT_VALUE)).getValue();

        expandedInstructions.add(new ZeroVariableInstruction(getVariable(), instructionLabel));
        for(int i = 0; i < constantValue; i++) {
            expandedInstructions.add(new IncreaseInstruction(getVariable()));
        }

        return expandedInstructions;
    }

    @Override
    public Instruction clone() {
        ConstantAssignmentInstruction copy = (ConstantAssignmentInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }
}
