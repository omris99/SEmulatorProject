package logic.model.mappers;

import logic.model.instruction.InstructionOld;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionData;
import logic.model.variable.VariableOld;
import logic.model.generated.SInstruction;
import logic.model.generated.SInstructionArgument;
import logic.model.generated.SInstructionArguments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: 1. HANDLE EXCEPTION

public class InstructionMapper{

    public static InstructionOld toDomain(SInstruction jaxbInstruction) {
        if (jaxbInstruction == null) {
            return null;
        }

        InstructionData details = InstructionData.fromNameAndType(
                jaxbInstruction.getName(),
                jaxbInstruction.getType());

        try{
            VariableOld variable = VariableOld.parse(jaxbInstruction.getSVariable());
            SInstructionArguments sInstructionArguments = jaxbInstruction.getSInstructionArguments();
            Map<InstructionArgument, String> arguments = null;

            if(sInstructionArguments != null){
                arguments = jaxbInstructionsArgumentToDomain(jaxbInstruction.getSInstructionArguments().getSInstructionArgument());
            }

//            if (jaxbInstruction.getSInstructionArguments().getSInstructionArgument() != null) {
//                arguments = jaxbInstructionsArgumentToDomain(jaxbInstruction.getSInstructionArguments().getSInstructionArgument());
//            }
            InstructionOld domainInstruction = new InstructionOld(
                    details,
                    variable,
                    jaxbInstruction.getSLabel(),
                    arguments);

            return domainInstruction;

        }catch (Exception e){
            System.out.println("take care of it");
        }
        return null;
    }


    private static Map<InstructionArgument, String> jaxbInstructionsArgumentToDomain(List<SInstructionArgument> jaxbInstructionsArguments) {
        if (jaxbInstructionsArguments == null || jaxbInstructionsArguments.isEmpty()) {
            return null;
        }

        Map<InstructionArgument, String> domainArguments = new HashMap<>();
        for (SInstructionArgument jaxbInstructionArgument : jaxbInstructionsArguments) {
            String argumentName = jaxbInstructionArgument.getName();
            domainArguments.put(InstructionArgument.fromXmlNameFormat(argumentName), jaxbInstructionArgument.getValue());
        }

        return domainArguments;
    }
}
