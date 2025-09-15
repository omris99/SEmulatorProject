package logic.model.argument.commaseperatedarguments;

import logic.model.argument.Argument;

public class CommaSeperatedArguments implements Argument {
    private final String arguments;

    public CommaSeperatedArguments(String arguments) {
        this.arguments = arguments;
    }
    @Override
    public String getRepresentation() {
        return arguments;
    }

    @Override
    public int getIndex() {
        return 0;
    }
}
