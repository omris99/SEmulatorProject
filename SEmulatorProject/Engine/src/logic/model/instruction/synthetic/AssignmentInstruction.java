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
import logic.model.instruction.basic.IncreaseInstruction;
import logic.model.instruction.basic.JumpNotZeroInstruction;
import logic.model.instruction.basic.NeutralInstruction;
import logic.model.program.Program;
import logic.utils.Utils;

import java.util.*;

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
    public List<Instruction> expand(Program program, Label instructionLabel){
        int maxLabelIndex = Utils.getMaxLabelIndex(program.getAllInstructionsLabels());
        int maxWorkVariableIndex = Utils.getMaxGeneralVariableIndex(program.getAllInstructionsWorkVariables());

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
