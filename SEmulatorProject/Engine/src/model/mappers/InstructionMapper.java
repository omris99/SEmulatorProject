package model.mappers;

import model.Instruction;
import model.InstructionArgument;
import model.InstructionDetails;
import model.Variable;
import model.generated.SInstruction;
import model.generated.SInstructionArgument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            List<InstructionArgument> arguments = jaxbInstructionsArgumentToDomain(jaxbInstruction.getSInstructionArguments().getSInstructionArgument());
            if (jaxbInstruction.getSInstructionArguments().getSInstructionArgument() == null) {


                arguments = jaxbInstructionsArgumentToDomain(jaxbInstruction.getSInstructionArguments().getSInstructionArgument());
            }
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


    private static List<InstructionArgument> jaxbInstructionsArgumentToDomain(List<SInstructionArgument> jaxbInstructionsArguments) {
        if (jaxbInstructionsArguments == null || jaxbInstructionsArguments.isEmpty()) {
            return null;
        }

        List<InstructionArgument> domainArguments = new ArrayList<>();
        for (SInstructionArgument jaxbInstructionArgument : jaxbInstructionsArguments) {
            domainArguments.add(InstructionArgumentMapper.toDomain(jaxbInstructionArgument));
        }
        return domainArguments;
    }
}
