package logic.model.mappers;

import logic.exceptions.ArgumentErrorType;
import logic.exceptions.InvalidArgumentException;
import logic.model.argument.Argument;
import logic.model.argument.ArgumentType;
import logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import logic.model.argument.constant.Constant;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.label.LabelImpl;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.generated.SInstruction;
import logic.model.generated.SInstructionArgument;
import logic.model.generated.SInstructionArguments;
import logic.model.instruction.Instruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionData;
import logic.model.instruction.basic.DecreaseInstruction;
import logic.model.instruction.basic.IncreaseInstruction;
import logic.model.instruction.basic.JumpNotZeroInstruction;
import logic.model.instruction.basic.NeutralInstruction;
import logic.model.instruction.synthetic.*;
import logic.model.program.Function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructionMapper{

    public static Instruction toDomain(SInstruction jaxbInstruction, List<Function> functions) {
        if (jaxbInstruction == null) {
            return null;
        }

        InstructionData details = InstructionData.fromNameAndType(
                jaxbInstruction.getName(),
                jaxbInstruction.getType());
        Variable variable = VariableImpl.parse(jaxbInstruction.getSVariable());
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


    private static Map<InstructionArgument, Argument> jaxbInstructionsArgumentToDomain(List<SInstructionArgument> jaxbInstructionsArguments, List<Function> functions) {
        if (jaxbInstructionsArguments == null || jaxbInstructionsArguments.isEmpty()) {
            return null;
        }

        Map<InstructionArgument, Argument> domainArguments = new HashMap<>();
        for (SInstructionArgument jaxbInstructionArgument : jaxbInstructionsArguments) {
            String argumentName = jaxbInstructionArgument.getName();
            InstructionArgument argumentType = InstructionArgument.fromXmlNameFormat(argumentName);
            switch (argumentType.getType()) {
                case LABEL:
                    if(jaxbInstructionArgument.getValue().toUpperCase().equals("EXIT")) {
                        domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), (Argument) FixedLabel.EXIT);
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
                    domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), VariableImpl.parse(jaxbInstructionArgument.getValue()));
                    break;
                case CONSTANT:
                    try{
                        domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new Constant(Integer.parseInt(jaxbInstructionArgument.getValue())));
                    } catch(NumberFormatException e){
                        throw new InvalidArgumentException(jaxbInstructionArgument.getValue(), ArgumentErrorType.CONSTANT_MUST_BE_A_NUMBER);
                    }
                    break;
                case FUNCTION:
                    boolean functionFound = false;

                    for(Function function : functions) {
                        if(function.getName().equals(jaxbInstructionArgument.getValue())) {
                            domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), function);
                            functionFound = true;
                            break;
                        }
                    }
                    if(!functionFound) {
                        throw new InvalidArgumentException(jaxbInstructionArgument.getValue(), ArgumentErrorType.FUNCTION_NOT_FOUND);
                    }
                    break;
                case COMMA_SEPERATED_ARGUMENTS:
                    domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new CommaSeperatedArguments(jaxbInstructionArgument.getValue()));
                    break;
            }

//            if(argumentType.getType().equals(ArgumentType.LABEL)) {
//                if(jaxbInstructionArgument.getValue().toUpperCase().equals("EXIT")) {
//                    domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), (Argument) FixedLabel.EXIT);
//                }
//                else {
//                    if(!jaxbInstructionArgument.getValue().toUpperCase().startsWith("L")){
//                        throw new InvalidArgumentException(jaxbInstructionArgument.getValue(), ArgumentErrorType.LABEL_MUST_START_WITH_L);
//                    }
//                    try {
//                        domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new LabelImpl(Integer.parseInt(jaxbInstructionArgument.getValue().substring(1))));
//                    } catch (NumberFormatException e) {
//                        throw new InvalidArgumentException(jaxbInstructionArgument.getValue(), ArgumentErrorType.LABEL_INDEX_CANT_PARSE_TO_NUMBER);
//                    }
//                }
//            }
//            else if(argumentType.getType().equals(ArgumentType.VARIABLE)) {
//                domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), VariableImpl.parse(jaxbInstructionArgument.getValue()));
//            }
//            else if(argumentType.getType().equals(ArgumentType.CONSTANT)) {
//                try{
//                    domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new Constant(Integer.parseInt(jaxbInstructionArgument.getValue())));
//                } catch(NumberFormatException e){
//                    throw new InvalidArgumentException(jaxbInstructionArgument.getValue(), ArgumentErrorType.CONSTANT_MUST_BE_A_NUMBER);
//                }
//            }
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

            default -> throw new IllegalArgumentException("Unknown instruction: " + name);
        };
    }
}
