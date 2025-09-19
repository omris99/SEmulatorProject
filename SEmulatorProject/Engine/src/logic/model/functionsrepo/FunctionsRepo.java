package logic.model.functionsrepo;

import logic.model.program.Function;
import logic.model.program.Program;

import java.util.HashMap;
import java.util.Map;

public class FunctionsRepo {
    private final Map<String, Function> functions = new HashMap<>();
    private final Map<String, String> userStringToFunctionName = new HashMap<>();
    private static final FunctionsRepo repo = new FunctionsRepo();

    private FunctionsRepo() {}

    public static FunctionsRepo getInstance() {
        return repo;
    }

    public Function getFunctionByName(String name){
        return functions.get(name);
    }

    public Function getFunctionByUserString(String userString){
        for(Function function : functions.values()){
            if(function.getRepresentation().equals(userString)){
                return function;
            }
        }
        return null;
    }

    public void addFunction(Function function){
        functions.put(function.getName(), function);
        userStringToFunctionName.put(function.getRepresentation(), function.getName());
    }

    public String getFunctionUserString(String name){
        Function function = functions.get(name);
        if(function != null){
            return function.getRepresentation();
        }
        return null;
    }

    public String getFunctionNameByUserString(String userString){
        return userStringToFunctionName.get(userString);

    }
}
