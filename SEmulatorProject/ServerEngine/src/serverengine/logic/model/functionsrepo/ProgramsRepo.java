package serverengine.logic.model.functionsrepo;

import clientserverdto.UploadedProgramDTO;
import serverengine.logic.model.program.Function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramsRepo {
    private final Map<String, UploadedProgram> programs = new HashMap<>();
    private final Map<String, UploadedProgram> functions = new HashMap<>();
    private final Map<String, String> userStringToProgramName = new HashMap<>();
    private static final ProgramsRepo repo = new ProgramsRepo();

    private ProgramsRepo() {}

    public static ProgramsRepo getInstance() {
        return repo;
    }

    public Function getFunctionByName(String name){
        if(functions.get(name) != null){
            return (Function) functions.get(name).getProgram();
        }
        return null;
    }

    public void addProgram(UploadedProgram program){
        programs.putIfAbsent(program.getName(), program);
        userStringToProgramName.putIfAbsent(program.getUserString(), program.getName());
    }

    public void addFunction(UploadedProgram function){
        functions.putIfAbsent(function.getName(), function);
        userStringToProgramName.putIfAbsent(function.getUserString(), function.getName());
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

    public UploadedProgram getProgramOrFunctionByName(String name){
        if(programs.containsKey(name)){
            return programs.get(name);
        }
        else {
            return functions.get(name);
        }
    }

    public boolean isFunctionUploadedByUser(String functionName, String username){
        UploadedProgram function = functions.get(functionName);
        if(function != null){
            return function.getUploadedBy().equals(username);
        }

        return false;
    }

}
