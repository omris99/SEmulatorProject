package logic.model.instruction;

import logic.model.Argument;
import logic.model.execution.ExecutionContext;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

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
//        long variableValue = context.getVariableValue(getVariable());
//        variableValue = 0;
//        context.updateVariable(getVariable(), variableValue);

        return null;
    }

    @Override
    public String getInstructionDisplayFormat() {
        return String.format("%s <- %s", getVariable(), arguments.get(InstructionArgument.ASSIGNED_VARIABLE));
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
