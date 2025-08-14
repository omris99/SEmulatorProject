package logic.model.mappers;

import logic.model.Argument;
import logic.model.instruction.*;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.label.LabelImpl;
import logic.model.variable.Variable;
import logic.model.generated.SInstruction;
import logic.model.generated.SInstructionArgument;
import logic.model.generated.SInstructionArguments;
import logic.model.variable.VariableImpl;
import logic.model.variable.VariableOld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: 1. HANDLE EXCEPTION

public class InstructionMapper{

    public static Instruction toDomain(SInstruction jaxbInstruction) {
        if (jaxbInstruction == null) {
            return null;
        }

        InstructionData details = InstructionData.fromNameAndType(
                jaxbInstruction.getName(),
                jaxbInstruction.getType());

        try{
            Variable variable = VariableImpl.parse(jaxbInstruction.getSVariable());
            String instructionLabelOnXml = jaxbInstruction.getSLabel();
            Label instructionLabel = FixedLabel.EMPTY;

            if (instructionLabelOnXml != null) {
                if(!instructionLabelOnXml.startsWith("L")) {
                    throw new IllegalArgumentException("Label: " + instructionLabelOnXml + " is invalid. Every Label Must Start With 'L" );
                }

                instructionLabel = new LabelImpl(Integer.parseInt(instructionLabelOnXml.substring(1)));
            }

            SInstructionArguments sInstructionArguments = jaxbInstruction.getSInstructionArguments();
            Map<InstructionArgument, Argument> arguments = null;

            if(sInstructionArguments != null){
                arguments = jaxbInstructionsArgumentToDomain(jaxbInstruction.getSInstructionArguments().getSInstructionArgument());
            }

//            if (jaxbInstruction.getSInstructionArguments().getSInstructionArgument() != null) {
//                arguments = jaxbInstructionsArgumentToDomain(jaxbInstruction.getSInstructionArguments().getSInstructionArgument());
//            }
            Instruction domainInstruction = createInstruction(details.name(), variable, instructionLabel, arguments);

//            Instruction domainInstruction = new Instruction(
//                    details,
//                    variable,
//                    jaxbInstruction.getSLabel(),
//                    arguments);

            return domainInstruction;

        }catch (Exception e){
            System.out.println("take care of it");
        }
        return null;
    }


    private static Map<InstructionArgument, Argument> jaxbInstructionsArgumentToDomain(List<SInstructionArgument> jaxbInstructionsArguments) {
        if (jaxbInstructionsArguments == null || jaxbInstructionsArguments.isEmpty()) {
            return null;
        }

        Map<InstructionArgument, Argument> domainArguments = new HashMap<>();
        for (SInstructionArgument jaxbInstructionArgument : jaxbInstructionsArguments) {
            String argumentName = jaxbInstructionArgument.getName();
            InstructionArgument argumentType = InstructionArgument.fromXmlNameFormat(argumentName);
            if(argumentType.getType().equals("label")) {
                domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new LabelImpl(Integer.parseInt(jaxbInstructionArgument.getValue().substring(1))));
            }
            else if(argumentType.getType().equals("variable")) {
                domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), VariableImpl.parse(jaxbInstructionArgument.getValue()));
            }

        }

        return domainArguments;
    }

    private static Instruction createInstruction(String name, Variable variable, Label label, Map<InstructionArgument, Argument> arguments) {
        return switch (name) {
            case "INCREASE" -> new IncreaseInstruction(variable);
            case "DECREASE" -> new DecreaseInstruction(variable);
            case "JUMP_NOT_ZERO" -> new JumpNotZeroInstruction(variable, arguments.get(InstructionArgument.JNZ_LABEL) ,label);
            case "NEUTRAL" -> new NeutralInstruction(variable);
            //            case "ZERO_VARIABLE":
//                instructionDisplayFormat = String.format("%s <- 0", variable);
//                break;
//            case "GOTO_LABEL":
//                instructionDisplayFormat = String.format("GOTO %s", arguments.get(InstructionArgument.GOTO_LABEL));
//                break;
//            case "ASSIGNMENT":
//                instructionDisplayFormat = String.format("%s <- %s", variable, arguments.get(InstructionArgument.ASSIGNED_VARIABLE));
//                break;
//            case "CONSTANT_ASSIGNMENT":
//                instructionDisplayFormat = String.format("%s <- %s", variable, arguments.get(InstructionArgument.CONSTANT_VALUE));
//                break;
//            case "JUMP_ZERO":
//                instructionDisplayFormat = String.format("IF %s = 0 GOTO %s", variable, arguments.get(InstructionArgument.JZ_LABEL));
//                break;
//            case "JUMP_EQUAL_CONSTANT":
//                instructionDisplayFormat = String.format("IF %s = %s GOTO %s", variable, arguments.get(InstructionArgument.JE_CONSTANT_LABEL), arguments.get(InstructionArgument.CONSTANT_VALUE));
//                break;
//            case "JUMP_EQUAL_VARIABLE":
//                instructionDisplayFormat = String.format("IF %s = %s GOTO %s", variable, arguments.get(InstructionArgument.JE_VARIABLE_LABEL), arguments.get(InstructionArgument.VARIABLE_NAME));
//                break;
            default -> throw new IllegalArgumentException("Unknown instruction: " + name);
        };
    }
}
