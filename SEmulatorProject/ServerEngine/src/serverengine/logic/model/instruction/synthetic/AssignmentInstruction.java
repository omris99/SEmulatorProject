package serverengine.logic.model.instruction.synthetic;

import clientserverdto.InstructionDTO;
import serverengine.logic.execution.ExecutionContext;
import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.label.LabelImpl;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.argument.variable.VariableImpl;
import serverengine.logic.model.argument.variable.VariableType;
import serverengine.logic.model.instruction.*;
import serverengine.logic.model.instruction.basic.DecreaseInstruction;
import serverengine.logic.model.instruction.basic.IncreaseInstruction;
import serverengine.logic.model.instruction.basic.JumpNotZeroInstruction;
import serverengine.logic.model.instruction.basic.NeutralInstruction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AssignmentInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Map<InstructionArgument, Argument> arguments;

    public AssignmentInstruction(Variable variable, Argument assignedVariable) {
        this(variable, assignedVariable, FixedLabel.EMPTY);
    }

    public AssignmentInstruction(Variable variable, Argument assignedVariable, Label label) {
        super(InstructionData.ASSIGNMENT, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.ASSIGNED_VARIABLE, assignedVariable);
    }

    @Override
    public Label execute(ExecutionContext context) {
        context.updateVariable(getVariable(), context.getVariableValue((Variable) arguments.get(InstructionArgument.ASSIGNED_VARIABLE)));

        return FixedLabel.EMPTY;
    }

    @Override
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("%s <- %s", getVariable().getRepresentation(), arguments.get(InstructionArgument.ASSIGNED_VARIABLE).getRepresentation());

        return super.getInstructionDTO(displayFormat);
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

    @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
        Label freeLabel1 = new LabelImpl(maxLabelIndex + 1);
        Label freeLabel2 = new LabelImpl(maxLabelIndex + 2);
        Label freeLabel3 = new LabelImpl(maxLabelIndex + 3);
        Variable workVariable = new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1);
        Variable variableForGoTo = new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 2);
        Variable assignedVariable = (Variable) arguments.get(InstructionArgument.ASSIGNED_VARIABLE);

        List<Instruction> expandedInstructions = new LinkedList<>();

        expandedInstructions.add(new ZeroVariableInstruction(getVariable(), instructionLabel));
        expandedInstructions.add(new JumpNotZeroInstruction(assignedVariable, freeLabel1));
        expandedInstructions.add(new GoToLabelInstruction(variableForGoTo, freeLabel3));
        expandedInstructions.add(new DecreaseInstruction(assignedVariable, freeLabel1));
        expandedInstructions.add(new IncreaseInstruction(workVariable));
        expandedInstructions.add(new JumpNotZeroInstruction(assignedVariable, freeLabel1));
        expandedInstructions.add(new DecreaseInstruction(workVariable, freeLabel2));
        expandedInstructions.add(new IncreaseInstruction(getVariable()));
        expandedInstructions.add(new IncreaseInstruction(assignedVariable));
        expandedInstructions.add(new JumpNotZeroInstruction(workVariable, freeLabel2));
        expandedInstructions.add(new NeutralInstruction(getVariable(), freeLabel3));

        return expandedInstructions;
    }

    @Override
    public Instruction clone() {
        AssignmentInstruction copy = (AssignmentInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }
}
