package logic.model.instruction.synthetic;

import logic.model.argument.Argument;
import logic.execution.ExecutionContext;
import logic.model.instruction.AbstractInstruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionData;
import logic.model.instruction.InstructionWithArguments;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class AssignmentInstruction extends AbstractInstruction implements InstructionWithArguments {
    Map<InstructionArgument, Argument> arguments;

    public AssignmentInstruction(Variable variable, Argument assignedVariable) {
        this(variable, assignedVariable, FixedLabel.EMPTY);
    }

    public AssignmentInstruction(Variable variable, Argument assignedVariable, Label label) {
        super(InstructionData.ASSIGNMENT, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.ASSIGNED_VARIABLE, assignedVariable);
//        this.jnzLabel = (Label)jnzLabel;
    }

    @Override
    public Label execute(ExecutionContext context) {
        context.updateVariable(getVariable(), context.getVariableValue((Variable) arguments.get(InstructionArgument.ASSIGNED_VARIABLE)));

        return FixedLabel.EMPTY;
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("%s <- %s", getVariable().getRepresentation(), arguments.get(InstructionArgument.ASSIGNED_VARIABLE).getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
