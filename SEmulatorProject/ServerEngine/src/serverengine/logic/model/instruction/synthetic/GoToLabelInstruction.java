package serverengine.logic.model.instruction.synthetic;

import clientserverdto.InstructionDTO;
import serverengine.logic.execution.ExecutionContext;
import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.argument.variable.VariableImpl;
import serverengine.logic.model.argument.variable.VariableType;
import serverengine.logic.model.instruction.*;
import serverengine.logic.model.instruction.basic.IncreaseInstruction;
import serverengine.logic.model.instruction.basic.JumpNotZeroInstruction;

import java.util.*;

public class GoToLabelInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
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
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("GOTO %s", arguments.get(InstructionArgument.GOTO_LABEL).getRepresentation());

        return super.getInstructionDTO(displayFormat);
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

    @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
        Variable workVariable = new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1);
        List<Instruction> expandedInstructions = new LinkedList<>();

        expandedInstructions.add(new IncreaseInstruction(workVariable, instructionLabel));
        expandedInstructions.add(new JumpNotZeroInstruction(workVariable, arguments.get(InstructionArgument.GOTO_LABEL)));

        return expandedInstructions;
    }

    @Override
    public Instruction clone() {
        GoToLabelInstruction copy = (GoToLabelInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }
}
