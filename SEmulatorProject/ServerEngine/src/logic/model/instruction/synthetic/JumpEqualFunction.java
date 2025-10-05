package logic.model.instruction.synthetic;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.model.argument.Argument;
import logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.functionsrepo.FunctionsRepo;
import logic.model.instruction.*;
import logic.model.program.Function;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JumpEqualFunction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Map<InstructionArgument, Argument> arguments;

    public JumpEqualFunction(Variable variable, Argument functionName, Argument functionArguments, Argument jeFunctionLabel) {
        this(variable, jeFunctionLabel, functionName, functionArguments, FixedLabel.EMPTY);
    }

    public JumpEqualFunction(Variable variable, Argument functionName, Argument functionArguments, Argument jeFunctionLabel, Label label) {
        super(InstructionData.JUMP_EQUAL_FUNCTION, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.JE_FUNCTION_LABEL, jeFunctionLabel);
        arguments.put(InstructionArgument.FUNCTION_NAME, functionName);
        arguments.put(InstructionArgument.FUNCTION_ARGUMENTS, functionArguments);
    }


        @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel) {
            List<Instruction> expandedInstructions = new LinkedList<>();
            Variable workVariable1 = new VariableImpl(VariableType.WORK, maxWorkVariableIndex + 1);

            expandedInstructions.add(new QuoteInstruction(workVariable1, arguments.get(InstructionArgument.FUNCTION_NAME), arguments.get(InstructionArgument.FUNCTION_ARGUMENTS), instructionLabel));
            expandedInstructions.add(new JumpEqualVariableInstruction(getVariable(), arguments.get(InstructionArgument.JE_FUNCTION_LABEL), workVariable1));

            return expandedInstructions;
        }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());
        Function contextFunction = FunctionsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());
        Long functionResult = contextFunction.run((CommaSeperatedArguments) arguments.get(InstructionArgument.FUNCTION_ARGUMENTS), context.getVariablesStatus());
        if(variableValue == functionResult){
            return (Label) arguments.get(InstructionArgument.JE_FUNCTION_LABEL);
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format(String.format("IF %s = (%s,%s) GOTO %s", getVariable().getRepresentation(),
                FunctionsRepo.getInstance().getFunctionUserString(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation()),
                ((CommaSeperatedArguments)arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)).getUserDisplayArguments(),
                arguments.get(InstructionArgument.JE_FUNCTION_LABEL).getRepresentation()));

        return super.getInstructionDisplayFormat(displayFormat);

    }

    @Override
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("IF %s = (%s,%s) GOTO %s", getVariable().getRepresentation(),
                FunctionsRepo.getInstance().getFunctionUserString(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation()),
                ((CommaSeperatedArguments)arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)).getUserDisplayArguments(),
                arguments.get(InstructionArgument.JE_FUNCTION_LABEL).getRepresentation());

        return getInstructionDTO(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }

    @Override
    public Instruction clone() {
        JumpEqualFunction copy = (JumpEqualFunction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }

    @Override
    public int getCycles(){
        int totalCycles = super.getCycles();
        Function contextFunction = FunctionsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());
        if(contextFunction != null) {
            totalCycles += contextFunction.getTotalCycles();
        }

        totalCycles += ((CommaSeperatedArguments) arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)).getTotalCycles();

        return totalCycles;
    }
}
