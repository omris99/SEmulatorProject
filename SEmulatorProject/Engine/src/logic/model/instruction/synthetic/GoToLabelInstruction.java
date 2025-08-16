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
        String displayFormat = String.format("GOTO %s", arguments.get(InstructionArgument.GOTO_LABEL).getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
