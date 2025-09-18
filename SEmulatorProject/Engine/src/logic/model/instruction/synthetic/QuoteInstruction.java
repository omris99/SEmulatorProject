package logic.model.instruction.synthetic;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.execution.ExecutionContextImpl;
import logic.execution.ProgramExecutor;
import logic.execution.ProgramExecutorImpl;
import logic.model.argument.Argument;
import logic.model.argument.NameArgument;
import logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.functionsrepo.FunctionsRepo;
import logic.model.instruction.*;
import logic.model.instruction.basic.NeutralInstruction;
import logic.model.program.Function;
import logic.model.program.QuotedFunction;

import java.util.*;

public class QuoteInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Map<InstructionArgument, Argument> arguments;

    public QuoteInstruction(Variable variable, Argument functionName, Argument functionArguments) {
        this(variable, functionName, functionArguments, FixedLabel.EMPTY);
    }

    public QuoteInstruction(Variable variable, Argument functionName, Argument functionArguments, Label label) {
        super(InstructionData.QUOTE, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.FUNCTION_NAME, functionName);
        arguments.put(InstructionArgument.FUNCTION_ARGUMENTS, functionArguments);
    }

    @Override
    public Label execute(ExecutionContext context) {
        Function functionToExecute = FunctionsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());
        context.updateVariable(getVariable(),
                functionToExecute.run(
                ((CommaSeperatedArguments) arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)),
                context.getVariablesStatus()));

        return FixedLabel.EMPTY;
    }

    @Override
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format(String.format("%s <- (%s,%s)", getVariable().getRepresentation(),
                arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation(),
                arguments.get(InstructionArgument.FUNCTION_ARGUMENTS).getRepresentation()));

        return super.getInstructionDTO(displayFormat);
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format(String.format("%s <- (%s,%s)", getVariable().getRepresentation(),
                arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation(),
                arguments.get(InstructionArgument.FUNCTION_ARGUMENTS).getRepresentation()));

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }


    @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
        Function contextFunction = FunctionsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());
        QuotedFunction quotedFunction = contextFunction.quote(
                maxWorkVariableIndex,
                maxLabelIndex,
                ((CommaSeperatedArguments)arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)),
                getVariable(),
                instructionLabel);

        return quotedFunction.getFullQuotedFunctionInstructions();
    }

    @Override
    public int getCycles(){
        int totalCycles = super.getCycles();
        Function contextFunction = FunctionsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());
        totalCycles += contextFunction.getCycles();
        return totalCycles;
    }


    @Override
    public Instruction clone() {
        QuoteInstruction copy = (QuoteInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }
}
