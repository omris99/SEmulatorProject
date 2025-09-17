package logic.model.argument.constant;

import logic.exceptions.ArgumentErrorType;
import logic.exceptions.InvalidArgumentException;
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

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public Argument parse(String stringArgument) {
            if(stringArgument == null){
                throw new InvalidArgumentException(stringArgument, ArgumentErrorType.CONSTANT_MUST_BE_A_NUMBER);
            }
            try {
                int intValue = Integer.parseInt(stringArgument);
                return new Constant(intValue);
            } catch (NumberFormatException e) {
                throw new InvalidArgumentException(stringArgument, ArgumentErrorType.CONSTANT_MUST_BE_A_NUMBER);
            }
    }
}
