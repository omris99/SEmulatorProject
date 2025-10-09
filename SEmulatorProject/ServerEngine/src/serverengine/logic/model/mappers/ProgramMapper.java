package serverengine.logic.model.mappers;

import exceptions.AlreadyExistsProgramException;
import serverengine.logic.model.functionsrepo.ProgramsRepo;
import serverengine.logic.model.functionsrepo.UploadedProgram;
import serverengine.logic.model.generated.SFunction;
import serverengine.logic.model.generated.SInstruction;
import serverengine.logic.model.generated.SProgram;
import serverengine.logic.model.program.Function;
import serverengine.logic.model.program.Program;
import serverengine.logic.model.program.ProgramImpl;

import java.util.ArrayList;
import java.util.List;

public class ProgramMapper {
    public static Program toDomain(String uploadedBy, SProgram jaxbProgram) {
        ProgramsRepo programsRepo = ProgramsRepo.getInstance();
        if (jaxbProgram == null) {
            return null;
        }

        ProgramImpl domainProgram = new ProgramImpl(jaxbProgram.getName());

        List<String> functionNames = new ArrayList<>();
        if(jaxbProgram.getSFunctions() != null) {
            for (SFunction jaxbFunction : jaxbProgram.getSFunctions().getSFunction()) {
                if(programsRepo.getProgramOrFunctionByName(jaxbFunction.getName()) != null){
                    throw new AlreadyExistsProgramException(jaxbFunction.getName(), true);
                }
                functionNames.add(jaxbFunction.getName());
            }
        }

        domainProgram.setFunctionsNames(functionNames);

        for(SInstruction instruction : jaxbProgram.getSInstructions().getSInstruction())
        {
            domainProgram.addInstruction(InstructionMapper.toDomain(instruction, functionNames));
        }

        if(!functionNames.isEmpty()){
            List<Function> functions = new ArrayList<>();
            for (SFunction jaxbFunction : jaxbProgram.getSFunctions().getSFunction()){
                Function function = FunctionMapper.toDomain(jaxbFunction, functionNames);
                functions.add(function);
            }
            for(Function function : functions){
                ProgramsRepo.getInstance().addFunction(new UploadedProgram(uploadedBy, function, jaxbProgram.getName()));
            }
        }

        return domainProgram;
    }
}
