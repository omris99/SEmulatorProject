package logic.model.instruction;

import logic.model.Argument;
import logic.model.execution.ExecutionContext;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class GoToLabelInstruction extends AbstractInstruction implements InstructionWithArguments {
    Map<InstructionArgument, Argument> arguments;

    public GoToLabelInstruction(Variable variable, Argument goToLabel) {
        this(variable, goToLabel, FixedLabel.EMPTY);
    }

    public GoToLabelInstruction(Variable variable, Argument goToLabel, Label label) {
        super(InstructionData.GOTO_LABEL, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.GOTO_LABEL, goToLabel);
    }

    @Override
    public Label execute(ExecutionContext context) {
        return (Label) arguments.get(InstructionArgument.GOTO_LABEL);
    }

    @Override
    public String getInstructionDisplayFormat() {
        return String.format("GOTO %s", arguments.get(InstructionArgument.GOTO_LABEL));
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
