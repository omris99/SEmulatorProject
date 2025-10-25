package serverengine.logic.model.mappers;

import serverengine.logic.model.generated.SFunction;
import serverengine.logic.model.generated.SInstruction;
import serverengine.logic.model.program.Function;

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
