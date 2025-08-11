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

public class InstructionMapper {
    public static Instruction toDomain(SInstruction jaxbInstruction) {
        if (jaxbInstruction == null) {
            return null;
        }

        InstructionDetails details = InstructionDetails.fromNameAndType(
                jaxbInstruction.getName(),
                jaxbInstruction.getType());
        try{
            Variable variable = Variable.parse(jaxbInstruction.getSVariable());

            Instruction domainInstruction = new Instruction(
                    details,
                    variable,
                    jaxbInstruction.getSLabel(),
                    jaxbInstruction.getSInstructionArguments().getSInstructionArgument());
            return domainInstruction;

        }catch (Exception e){
            System.out.println("take care of it");
        }
    }

    private List<InstructionArgument> jaxbInstructionsArgumentToDomain(List<SInstructionArgument> jaxbInstructionsArgument)
    {
        List<InstructionArgument> domainArguments = new ArrayList<>();
        for (SInstructionArgument argument : jaxbInstructionsArgument) {
            InstructionArgument domainArgument = new InstructionArgument();

        }
    }
}
