package serverengine.logic.model.mappers;

import types.errortypes.ArgumentErrorType;
import exceptions.InvalidArgumentException;
import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.NameArgument;
import serverengine.logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import serverengine.logic.model.argument.constant.Constant;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.label.LabelImpl;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.argument.variable.VariableImpl;
import serverengine.programs.repo.ProgramsRepo;
import serverengine.logic.model.generated.SInstruction;
import serverengine.logic.model.generated.SInstructionArgument;
import serverengine.logic.model.generated.SInstructionArguments;
import serverengine.logic.model.instruction.Instruction;
import serverengine.logic.model.instruction.InstructionArgument;
import serverengine.logic.model.instruction.InstructionData;
import serverengine.logic.model.instruction.basic.DecreaseInstruction;
import serverengine.logic.model.instruction.basic.IncreaseInstruction;
import serverengine.logic.model.instruction.basic.JumpNotZeroInstruction;
import serverengine.logic.model.instruction.basic.NeutralInstruction;
import serverengine.logic.model.instruction.synthetic.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructionMapper{

    public static Instruction toDomain(SInstruction jaxbInstruction, List<String> functions) {
        if (jaxbInstruction == null) {
            return null;
        }

        InstructionData details = InstructionData.fromNameAndType(
                jaxbInstruction.getName(),
                jaxbInstruction.getType());
        Variable variable = new VariableImpl(jaxbInstruction.getSVariable());
        String instructionLabelOnXml = jaxbInstruction.getSLabel();
        Label instructionLabel = FixedLabel.EMPTY;

        if (instructionLabelOnXml != null) {
            instructionLabelOnXml = instructionLabelOnXml.toUpperCase();
            if(!instructionLabelOnXml.startsWith("L")) {
                throw new InvalidArgumentException(instructionLabelOnXml, ArgumentErrorType.LABEL_MUST_START_WITH_L);
            }
            try{
                instructionLabel = new LabelImpl(Integer.parseInt(instructionLabelOnXml.substring(1)));
            } catch(NumberFormatException e) {
                throw new InvalidArgumentException(instructionLabelOnXml, ArgumentErrorType.LABEL_INDEX_CANT_PARSE_TO_NUMBER);
            }
        }

        SInstructionArguments sInstructionArguments = jaxbInstruction.getSInstructionArguments();
        Map<InstructionArgument, Argument> arguments = null;

        if(sInstructionArguments != null){
            arguments = jaxbInstructionsArgumentToDomain(jaxbInstruction.getSInstructionArguments().getSInstructionArgument(), functions);
        }

        Instruction domainInstruction = createInstruction(details.name(), variable, instructionLabel, arguments);

        return domainInstruction;
    }


    private static Map<InstructionArgument, Argument> jaxbInstructionsArgumentToDomain(List<SInstructionArgument> jaxbInstructionsArguments, List<String> functions) {
        if (jaxbInstructionsArguments == null || jaxbInstructionsArguments.isEmpty()) {
            return null;
        }

        Map<InstructionArgument, Argument> domainArguments = new HashMap<>();
        for (SInstructionArgument jaxbInstructionArgument : jaxbInstructionsArguments) {
            String argumentName = jaxbInstructionArgument.getName();
            InstructionArgument argumentType = InstructionArgument.fromXmlNameFormat(argumentName);
            switch (argumentType.getType()) {
                case LABEL:
                    if(jaxbInstructionArgument.getValue().equalsIgnoreCase("EXIT")) {
                        domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), FixedLabel.EXIT);
                    }
                    else {
                        if(!jaxbInstructionArgument.getValue().toUpperCase().startsWith("L")){
                            throw new InvalidArgumentException(jaxbInstructionArgument.getValue(), ArgumentErrorType.LABEL_MUST_START_WITH_L);
                        }
                        try {
                            domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new LabelImpl(Integer.parseInt(jaxbInstructionArgument.getValue().substring(1))));
                        } catch (NumberFormatException e) {
                            throw new InvalidArgumentException(jaxbInstructionArgument.getValue(), ArgumentErrorType.LABEL_INDEX_CANT_PARSE_TO_NUMBER);
                        }
                    }
                    break;
                case VARIABLE:
                    domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new VariableImpl(jaxbInstructionArgument.getValue()));
                    break;
                case CONSTANT:
                    try{
                        domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new Constant(Integer.parseInt(jaxbInstructionArgument.getValue())));
                    } catch(NumberFormatException e){
                        throw new InvalidArgumentException(jaxbInstructionArgument.getValue(), ArgumentErrorType.CONSTANT_MUST_BE_A_NUMBER);
                    }
                    break;
                case NAME:
                    boolean functionFound = false;

                    if(ProgramsRepo.getInstance().getFunctionByName(jaxbInstructionArgument.getValue()) != null){
                        domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new NameArgument(jaxbInstructionArgument.getValue()));
                        functionFound = true;
                    }
                    else{
                        for(String functionName : functions) {
                            if(functionName.equals(jaxbInstructionArgument.getValue())) {
                                domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new NameArgument(functionName));
                                functionFound = true;
                                break;
                            }
                        }
                    }

                    if(!functionFound) {
                        throw new InvalidArgumentException(jaxbInstructionArgument.getValue(), ArgumentErrorType.FUNCTION_NOT_FOUND);
                    }
                    break;

                case COMMA_SEPERATED_ARGUMENTS:
                    CommaSeperatedArguments arguments = new CommaSeperatedArguments(jaxbInstructionArgument.getValue());
                    List<String> extractedFunctionsNames = arguments.extractAllFunctionsNames();
                    for(String extractedFunctionName : extractedFunctionsNames) {
                        if((!functions.contains(extractedFunctionName)) && ProgramsRepo.getInstance().getFunctionByName(extractedFunctionName) == null) {
                            throw new InvalidArgumentException(extractedFunctionName, ArgumentErrorType.FUNCTION_NOT_FOUND);
                        }
                    }
                    domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), arguments);
                    break;
            }
        }

        return domainArguments;
    }

    public static Instruction createInstruction(String name, Variable variable, Label label, Map<InstructionArgument, Argument> arguments) {
        return switch (name) {
            case "INCREASE" -> new IncreaseInstruction(variable, label);
            case "DECREASE" -> new DecreaseInstruction(variable, label);
            case "JUMP_NOT_ZERO" -> new JumpNotZeroInstruction(variable, arguments.get(InstructionArgument.JNZ_LABEL) ,label);
            case "NEUTRAL" -> new NeutralInstruction(variable, label);
            case "ZERO_VARIABLE" -> new ZeroVariableInstruction(variable, label);
            case "GOTO_LABEL" -> new GoToLabelInstruction(variable, arguments.get(InstructionArgument.GOTO_LABEL), label);
            case "ASSIGNMENT" -> new AssignmentInstruction(variable, arguments.get(InstructionArgument.ASSIGNED_VARIABLE), label);
            case "CONSTANT_ASSIGNMENT" -> new ConstantAssignmentInstruction(variable, arguments.get(InstructionArgument.CONSTANT_VALUE), label);
            case "JUMP_ZERO" -> new JumpZeroInstruction(variable, arguments.get(InstructionArgument.JZ_LABEL), label);
            case "JUMP_EQUAL_CONSTANT" -> new JumpEqualConstantInstruction(variable, arguments.get(InstructionArgument.JE_CONSTANT_LABEL), arguments.get(InstructionArgument.CONSTANT_VALUE), label);
            case "JUMP_EQUAL_VARIABLE" -> new JumpEqualVariableInstruction(variable, arguments.get(InstructionArgument.JE_VARIABLE_LABEL), arguments.get(InstructionArgument.VARIABLE_NAME), label);
            case "QUOTE" -> new QuoteInstruction(variable, arguments.get(InstructionArgument.FUNCTION_NAME), arguments.get(InstructionArgument.FUNCTION_ARGUMENTS), label);
            case "JUMP_EQUAL_FUNCTION" -> new JumpEqualFunction(variable, arguments.get(InstructionArgument.FUNCTION_NAME), arguments.get(InstructionArgument.FUNCTION_ARGUMENTS), arguments.get(InstructionArgument.JE_FUNCTION_LABEL), label);
            default -> throw new IllegalArgumentException("Unknown instruction: " + name);
        };
    }
}
