package logic.model.argument.commaseperatedarguments;

import logic.model.argument.Argument;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.functionsrepo.FunctionsRepo;
import logic.model.program.Function;

import java.util.*;

public class CommaSeperatedArguments implements Argument {
    private String arguments;
    List<Argument> argumentList;

    public CommaSeperatedArguments(String arguments) {
        this.arguments = arguments;
        this.argumentList = convertCommaSeperatedArgumentsStringToArgumentsList(arguments);
    }

    @Override
    public String getRepresentation() {
        return arguments;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    private List<Argument> convertCommaSeperatedArgumentsStringToArgumentsList(String arguments) {
        List<String> extractedArguments = extractArguments();
        List<Argument> argumentList = new ArrayList<>();
        for(String stringArgument : extractedArguments){
            if(VariableImpl.stringVarTypeToVariableType(stringArgument.substring(0,1)) != null){
                argumentList.add(new VariableImpl(stringArgument));
            }else if(stringArgument.startsWith("(") && stringArgument.endsWith(")")){
                argumentList.add(new CommaSeperatedArguments(stringArgument.substring(1, stringArgument.length() - 1)));
            }else{
//                argumentList.add(Functions.getFunctionByName(stringArgument));
            }
        }
        return argumentList;
    }



    public Set<Variable> detectInputVariables() {
        Set<Variable> inputVariables = new HashSet<>();
        List<String> splitedArgumentsString = extractArguments();
        for (String argument : splitedArgumentsString) {
            if (VariableImpl.stringVarTypeToVariableType(argument.substring(0,1)) == VariableType.INPUT) {
                inputVariables.add(new VariableImpl(argument));
            }else if(argument.startsWith("(") && argument.endsWith(")")) {
                CommaSeperatedArguments nestedArguments = new CommaSeperatedArguments(argument.substring(1, argument.length() - 1));
                inputVariables.addAll(nestedArguments.detectInputVariables());
            }
        }

        return inputVariables;
    }

    public List<String> extractArguments() {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;

        for (char c : arguments.toCharArray()) {
            if (c == '(') {
                depth++;
                current.append(c);
            } else if (c == ')') {
                depth--;
                current.append(c);

                if (depth == 0) {
                    parts.add(current.toString().trim());
                    current.setLength(0);
                }
            } else if (c == ',' && depth == 0) {
                if (!current.isEmpty()) {
                    parts.add(current.toString().trim());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            parts.add(current.toString().trim());
        }

        return parts;
    }

    @Override
    public Argument parse(String stringArgument) {
        return new CommaSeperatedArguments(stringArgument);
    }

    public List<Argument> getArgumentList() {
        return argumentList;
    }

    public CommaSeperatedArguments changeInputsToActualVariables(Map<Variable, Variable> variableMapping){
        List<String> extractedArguments = extractArguments();
        List<String> newArguments = new LinkedList<>();
        for(String argument : extractedArguments){
            if(VariableImpl.stringVarTypeToVariableType(argument.substring(0,1)) != null){
                newArguments.add(variableMapping.get(new VariableImpl(argument)).getRepresentation());
            }
            else if(argument.startsWith("(") && argument.endsWith(")")){
                CommaSeperatedArguments nestedArguments = new CommaSeperatedArguments(argument.substring(1, argument.length() - 1));
                newArguments.add("("+String.join(",", nestedArguments.changeInputsToActualVariables(variableMapping).extractArguments())+")");
            }
            else {
                newArguments.add(argument);
            }
        }

        return new CommaSeperatedArguments(String.join(",", newArguments));
    }

    public String getUserDisplayArguments(){
        List<String> extractedArguments = extractArguments();
        List<String> newArguments = new LinkedList<>();
        for(String argument : extractedArguments){
            if(VariableImpl.stringVarTypeToVariableType(argument.substring(0,1)) != null){
                newArguments.add(argument);
            }
            else if(argument.startsWith("(") && argument.endsWith(")")){
                CommaSeperatedArguments nestedArguments = new CommaSeperatedArguments(argument.substring(1, argument.length() - 1));
                newArguments.add("("+String.join(",", nestedArguments.getUserDisplayArguments())+")");
            }
            else {
                newArguments.add(FunctionsRepo.getInstance().getFunctionUserString(argument));
            }
        }

        return String.join(",", newArguments);
    }
}
