package logic.model.argument;

public class NameArgument implements Argument {
    String name;

    public NameArgument(String name) {
        this.name = name;
    }

    @Override
    public String getRepresentation() {
        return name;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public Argument parse(String stringArgument) {
        return new NameArgument(stringArgument);
    }
}
