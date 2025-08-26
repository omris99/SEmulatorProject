package logic.model.mappers;

import logic.model.program.Program;
import logic.model.generated.SInstruction;
import logic.model.generated.SProgram;
import logic.model.program.ProgramImpl;

public class ProgramMapper {
    public static Program toDomain(SProgram jaxbProgram) {
        if (jaxbProgram == null) {
            return null;
        }

        Program domainProgram = new ProgramImpl(jaxbProgram.getName());

        for(SInstruction instruction : jaxbProgram.getSInstructions().getSInstruction())
        {
            domainProgram.addInstruction(InstructionMapper.toDomain(instruction));
        }

        return domainProgram;
    }
}
