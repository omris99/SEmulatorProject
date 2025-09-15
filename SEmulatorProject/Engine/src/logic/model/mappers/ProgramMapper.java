package logic.model.mappers;

import logic.model.generated.SFunction;
import logic.model.generated.SInstruction;
import logic.model.generated.SProgram;
import logic.model.program.Program;
import logic.model.program.ProgramImpl;

public class ProgramMapper {
    public static Program toDomain(SProgram jaxbProgram) {
        if (jaxbProgram == null) {
            return null;
        }

        ProgramImpl domainProgram = new ProgramImpl(jaxbProgram.getName());

        for (SFunction jaxbFunction : jaxbProgram.getSFunctions().getSFunction()){
            domainProgram.addFunction(FunctionMapper.toDomain(jaxbFunction));
        }

        for(SInstruction instruction : jaxbProgram.getSInstructions().getSInstruction())
        {
            domainProgram.addInstruction(InstructionMapper.toDomain(instruction, domainProgram.getFunctions()));
        }

        return domainProgram;
    }
}
