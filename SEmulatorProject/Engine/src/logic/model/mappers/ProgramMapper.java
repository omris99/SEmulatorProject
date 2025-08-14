package logic.model.mappers;

import logic.model.instruction.InstructionOld;
import logic.model.instruction.Instructions;
import logic.model.program.ProgramOld;
import logic.model.generated.SInstruction;
import logic.model.generated.SProgram;

import java.util.ArrayList;
import java.util.List;

public class ProgramMapper {
    public static ProgramOld toDomain(SProgram jaxbProgram) {
        if (jaxbProgram == null) {
            return null;
        }

        List<InstructionOld> instructionsList = new ArrayList<>();

        for(SInstruction instruction : jaxbProgram.getSInstructions().getSInstruction())
        {
            instructionsList.add(InstructionMapper.toDomain(instruction));
        }

        Instructions instructions = new Instructions(instructionsList);


        return new ProgramOld(jaxbProgram.getName(), instructions);
    }
}
