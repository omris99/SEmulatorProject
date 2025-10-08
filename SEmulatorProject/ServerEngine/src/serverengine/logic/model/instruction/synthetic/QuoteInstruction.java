package serverengine.logic.model.instruction.synthetic;

import clientserverdto.InstructionDTO;
import serverengine.logic.execution.ExecutionContext;
import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.NameArgument;
import serverengine.logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.argument.variable.VariableImpl;
import serverengine.logic.model.argument.variable.VariableType;
import serverengine.logic.model.functionsrepo.ProgramsRepo;
import serverengine.logic.model.instruction.*;
import serverengine.logic.model.instruction.basic.NeutralInstruction;
import serverengine.logic.model.program.Function;
import serverengine.logic.model.program.quotedfunction.QuotedFunction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QuoteInstruction extends AbstractInstruction implements InstructionWithArguments, ExpandableInstruction {
    Map<InstructionArgument, Argument> arguments;
    InstructionDTO cachedInstructionDTO;

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
        Function functionToExecute = ProgramsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());
        context.updateVariable(getVariable(), functionToExecute.run(((CommaSeperatedArguments) arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)), context.getVariablesStatus()));
        return FixedLabel.EMPTY;
    }

    @Override
    public InstructionDTO getInstructionDTO() {
        if (cachedInstructionDTO != null) {
            if(getBreakpoint() == cachedInstructionDTO.getIsBreakpointSet()){
                return cachedInstructionDTO;
            }
        }

        String displayFormat = String.format(String.format("%s <- (%s,%s)", getVariable().getRepresentation(),
                ProgramsRepo.getInstance().getFunctionUserString(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation()),
                ((CommaSeperatedArguments)arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)).getUserDisplayArguments()));
        InstructionDTO newInstructionDTO = super.getInstructionDTO(displayFormat);
        cachedInstructionDTO = newInstructionDTO;

        return newInstructionDTO;
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format(String.format("%s <- (%s,%s)", getVariable().getRepresentation(),
                ProgramsRepo.getInstance().getFunctionUserString(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation()),
                ((CommaSeperatedArguments)arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)).getUserDisplayArguments()));

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }


    @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
        Function contextFunction = ProgramsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());

        QuotedFunction quotedFunction = contextFunction.quote(maxWorkVariableIndex, maxLabelIndex);
        List<Instruction> expandedInstructions = quotedFunction.getQuotedFunctionInstructions();

        List<String> contextFunctionArguments = ((CommaSeperatedArguments)arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)).extractArguments();

        List<Instruction> initialInputVariablesValues = new LinkedList<>();
        int inputIndex = 1;
        for(String argument : contextFunctionArguments){
            if (VariableImpl.stringVarTypeToVariableType(argument.substring(0,1)) != null) {
                initialInputVariablesValues.add(new AssignmentInstruction(quotedFunction.getOriginalVariablesToFreeWorkVariablesMap().get(new VariableImpl(VariableType.INPUT, inputIndex)), new VariableImpl(argument)));
            }else if(argument.startsWith("(") && argument.endsWith(")")) {
                CommaSeperatedArguments nestedArguments = new CommaSeperatedArguments(argument.substring(1, argument.length() - 1));
                List<String> functionCallargumentsList = nestedArguments.extractArguments();
                functionCallargumentsList.removeFirst();
                String commaSeperatedFunctionCallArguments = String.join(",", functionCallargumentsList);
                initialInputVariablesValues.add(new QuoteInstruction(quotedFunction.getOriginalVariablesToFreeWorkVariablesMap().get(new VariableImpl(VariableType.INPUT, inputIndex)), new NameArgument(nestedArguments.extractArguments().getFirst()), new CommaSeperatedArguments(commaSeperatedFunctionCallArguments)));
            }

            inputIndex++;
        }

        expandedInstructions.addAll(0, initialInputVariablesValues);
        expandedInstructions.addFirst(new NeutralInstruction(Variable.RESULT, instructionLabel));

        expandedInstructions.add(new AssignmentInstruction(
                getVariable(),
                quotedFunction.getOriginalVariablesToFreeWorkVariablesMap().get(Variable.RESULT),
                quotedFunction.getOriginalLabelsToFreeLabels().getOrDefault(FixedLabel.EXIT, FixedLabel.EMPTY)));

        return expandedInstructions;
    }


    @Override
    public Instruction clone() {
        QuoteInstruction copy = (QuoteInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }

    @Override
    public int getCycles(){
        int totalCycles = super.getCycles();
        Function contextFunction = ProgramsRepo.getInstance().getFunctionByName(arguments.get(InstructionArgument.FUNCTION_NAME).getRepresentation());
        if(contextFunction != null) {
            totalCycles += contextFunction.getTotalCycles();
        }

        totalCycles += ((CommaSeperatedArguments) arguments.get(InstructionArgument.FUNCTION_ARGUMENTS)).getTotalCycles();

        return totalCycles;
    }
}
