package logic.model.mappers;

import logic.model.argument.Argument;
import logic.model.argument.constant.Constant;
import logic.model.instruction.*;
import logic.model.instruction.basic.DecreaseInstruction;
import logic.model.instruction.basic.IncreaseInstruction;
import logic.model.instruction.basic.JumpNotZeroInstruction;
import logic.model.instruction.basic.NeutralInstruction;
import logic.model.instruction.synthetic.*;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.label.LabelImpl;
import logic.model.argument.variable.Variable;
import logic.model.generated.SInstruction;
import logic.model.generated.SInstructionArgument;
import logic.model.generated.SInstructionArguments;
import logic.model.argument.variable.VariableImpl;

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
                instructionLabelOnXml = instructionLabelOnXml.toUpperCase();
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

            Instruction domainInstruction = createInstruction(details.name(), variable, instructionLabel, arguments);

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
                if(jaxbInstructionArgument.getValue().toUpperCase().equals("EXIT")) {
                    domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), (Argument) FixedLabel.EXIT);
                }
                else {
                    domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new LabelImpl(Integer.parseInt(jaxbInstructionArgument.getValue().substring(1))));
                }
            }
            else if(argumentType.getType().equals("variable")) {
                domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), VariableImpl.parse(jaxbInstructionArgument.getValue()));
            }
            else if(argumentType.getType().equals("constant")) {
                domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), new Constant(Integer.parseInt(jaxbInstructionArgument.getValue())));
            }
        }

        return domainArguments;
    }

    private static Instruction createInstruction(String name, Variable variable, Label label, Map<InstructionArgument, Argument> arguments) {
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

            default -> throw new IllegalArgumentException("Unknown instruction: " + name);
        };
    }
}
