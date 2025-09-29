package logic.model.mappers;

import logic.model.generated.SFunction;
import logic.model.generated.SInstruction;
import logic.model.generated.SProgram;
import logic.model.program.Function;
import logic.model.program.Program;
import logic.model.program.ProgramImpl;

import java.util.List;

public class FunctionMapper {
    public static Function toDomain(SFunction jaxbProgram, List<String> functionNames) {
        if (jaxbProgram == null) {
            return null;
        }

        Function domainFunction = new Function(jaxbProgram.getName(), jaxbProgram.getUserString());

        for(SInstruction instruction : jaxbProgram.getSInstructions().getSInstruction()) {
            domainFunction.addInstruction(InstructionMapper.toDomain(instruction, functionNames));
        }

        return domainFunction;
    }
}
