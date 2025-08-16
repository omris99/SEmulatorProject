package logic.model.argument.constant;

import logic.model.argument.Argument;

public class Constant implements Argument {
    int value;

    public Constant(int value) {
        this.value = value;
    }

    @Override
    public String getRepresentation() {
        return Integer.toString(value);
    }

    public int getValue() {
        return value;
    }
}
