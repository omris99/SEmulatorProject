package serverengine.logic.model.argument.constant;

import serverengine.logic.model.argument.Argument;

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

    @Override
    public int getIndex() {
        return 0;
    }
}
