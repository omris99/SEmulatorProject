package logic.model.functionsrepo;

import logic.model.program.Function;

import java.util.HashMap;
import java.util.Map;

public class FunctionsRepo {
    private final Map<String, Function> functions = new HashMap<>();
    private static final FunctionsRepo repo = new FunctionsRepo();

    private FunctionsRepo() {}

    public static FunctionsRepo getInstance() {
        return repo;
    }

    public Function getFunctionByName(String name){
        return functions.get(name);
    }

    public void addFunction(Function function){
        functions.put(function.getName(), function);
    }

    public boolean isFunctionExist(String name){
        return functions.containsKey(name);
    }

}
