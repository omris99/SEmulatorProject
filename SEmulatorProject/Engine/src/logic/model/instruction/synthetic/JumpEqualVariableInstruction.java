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
import logic.model.instruction.basic.DecreaseInstruction;
import logic.model.instruction.basic.NeutralInstruction;
import logic.model.program.Function;

import java.util.*;

public class JumpEqualVariableInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Map<InstructionArgument, Argument> arguments;

    public JumpEqualVariableInstruction(Variable variable, Argument jeVariableLabel, Argument variableName) {
        this(variable, jeVariableLabel, variableName, FixedLabel.EMPTY);
    }

    public JumpEqualVariableInstruction(Variable variable, Argument jeVariableLabel, Argument variableName, Label label) {
        super(InstructionData.JUMP_EQUAL_VARIABLE, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.JE_VARIABLE_LABEL, jeVariableLabel);
        arguments.put(InstructionArgument.VARIABLE_NAME, variableName);
    }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());

        if (variableValue == context.getVariableValue((Variable)arguments.get(InstructionArgument.VARIABLE_NAME))) {
            return (Label) arguments.get(InstructionArgument.JE_VARIABLE_LABEL);
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("IF %s = %s GOTO %s",
                getVariable().getRepresentation(), arguments.get(InstructionArgument.VARIABLE_NAME).getRepresentation(),
                arguments.get(InstructionArgument.JE_VARIABLE_LABEL).getRepresentation());

        return getInstructionDTO(displayFormat);
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("IF %s = %s GOTO %s",
                getVariable().getRepresentation(), arguments.get(InstructionArgument.VARIABLE_NAME).getRepresentation(),
                arguments.get(InstructionArgument.JE_VARIABLE_LABEL).getRepresentation());

        return getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }

    @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
        List<Instruction> expandedInstructions = new LinkedList<>();
        Label freeLabel1 = new LabelImpl(maxLabelIndex + 1);
        Label freeLabel2 = new LabelImpl(maxLabelIndex + 2);
        Label freeLabel3 = new LabelImpl(maxLabelIndex + 3);
        Variable workVariable1 = new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1);
        Variable workVariable2 = new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 2);
        Variable goToWorkVariable = new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 3);

        expandedInstructions.add(new AssignmentInstruction(workVariable1, getVariable(), instructionLabel));
        expandedInstructions.add(new AssignmentInstruction(workVariable2, arguments.get(InstructionArgument.VARIABLE_NAME)));
        expandedInstructions.add(new JumpZeroInstruction(workVariable1, freeLabel3, freeLabel2));
        expandedInstructions.add(new JumpZeroInstruction(workVariable2, freeLabel1));
        expandedInstructions.add(new DecreaseInstruction(workVariable1));
        expandedInstructions.add(new DecreaseInstruction(workVariable2));
        expandedInstructions.add(new GoToLabelInstruction(goToWorkVariable, freeLabel2));
        expandedInstructions.add(new JumpZeroInstruction(workVariable2, arguments.get(InstructionArgument.JE_VARIABLE_LABEL), freeLabel3));
        expandedInstructions.add(new NeutralInstruction(Variable.RESULT, freeLabel1));

        return expandedInstructions;
    }


    @Override
    public Instruction clone() {
        JumpEqualVariableInstruction copy = (JumpEqualVariableInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }
}
