package model.mappers;

import model.Instruction;
import model.InstructionDetails;
import model.Variable;
import model.generated.SInstruction;
import model.generated.SInstructionArgument;
import model.generated.SInstructionArguments;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: 1. HANDLE EXCEPTION

public class InstructionMapper{

    public static Instruction toDomain(SInstruction jaxbInstruction) {
        if (jaxbInstruction == null) {
            return null;
        }

        InstructionDetails details = InstructionDetails.fromNameAndType(
                jaxbInstruction.getName(),
                jaxbInstruction.getType());

        try{
            Variable variable = Variable.parse(jaxbInstruction.getSVariable());
            SInstructionArguments sInstructionArguments = jaxbInstruction.getSInstructionArguments();
            Map<String, String> arguments = null;

            if(sInstructionArguments != null){
                arguments = jaxbInstructionsArgumentToDomain(jaxbInstruction.getSInstructionArguments().getSInstructionArgument());
            }

//            if (jaxbInstruction.getSInstructionArguments().getSInstructionArgument() != null) {
//                arguments = jaxbInstructionsArgumentToDomain(jaxbInstruction.getSInstructionArguments().getSInstructionArgument());
//            }
            Instruction domainInstruction = new Instruction(
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


    private static Map<String, String> jaxbInstructionsArgumentToDomain(List<SInstructionArgument> jaxbInstructionsArguments) {
        if (jaxbInstructionsArguments == null || jaxbInstructionsArguments.isEmpty()) {
            return null;
        }

        Map<String, String> domainArguments = new HashMap<>();
        for (SInstructionArgument jaxbInstructionArgument : jaxbInstructionsArguments) {
            domainArguments.put(jaxbInstructionArgument.getName(), jaxbInstructionArgument.getValue());
        }

        return domainArguments;
    }
}
