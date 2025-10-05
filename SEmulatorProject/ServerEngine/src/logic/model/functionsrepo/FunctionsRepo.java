package logic.model.functionsrepo;

import dto.ProgramDTO;
import dto.UploadedProgramDTO;
import logic.model.program.Function;
import logic.model.program.Program;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionsRepo {
    private final Map<String, UploadedProgram> programs = new HashMap<>();
    private final Map<String, UploadedProgram> functions = new HashMap<>();
    private final Map<String, String> userStringToProgramName = new HashMap<>();
    private static final FunctionsRepo repo = new FunctionsRepo();

    private FunctionsRepo() {}

    public static FunctionsRepo getInstance() {
        return repo;
    }

    public Function getFunctionByName(String name){
        if(functions.get(name) != null){
            return (Function) functions.get(name).getProgram();
        }
        return null;
    }

    public void addProgram(UploadedProgram program){
        programs.put(program.getName(), program);
    }

    public void addFunction(UploadedProgram function){
        functions.put(function.getName(), function);
        userStringToProgramName.put(function.getName(), function.getName());
    }

    public String getFunctionUserString(String name){
        UploadedProgram function = functions.get(name);
        if(function != null){
            return ((Function) function.getProgram()).getRepresentation();
        }
        return null;
    }
    public String getFunctionNameByUserString(String userString){
        return userStringToProgramName.get(userString);
    }

    public List<UploadedProgramDTO> getAllFunctions(){
        return functions.values().stream().map(UploadedProgram::createDTO).toList();
    }

    public List<UploadedProgramDTO> getAllPrograms(){
        return programs.values().stream().map(UploadedProgram::createDTO).toList();
    }

}
