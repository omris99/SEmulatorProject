package logic.model.argument.label;

import logic.exceptions.ArgumentErrorType;
import logic.exceptions.InvalidArgumentException;
import logic.model.argument.Argument;

import java.util.Objects;

public class LabelImpl implements Label {
    private final String label;
    private final int index;

    public LabelImpl(int number) {
        label = "L" + number;
        this.index = number;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String getRepresentation() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LabelImpl label1 = (LabelImpl) o;
        return Objects.equals(label, label1.label);
    }

    @Override
    public Argument parse(String stringArgument) {
        if(stringArgument == null || stringArgument.length() < 2 || stringArgument.charAt(0) != 'L'){
            throw new InvalidArgumentException(stringArgument, ArgumentErrorType.LABEL_MUST_START_WITH_L);
        }
        return new LabelImpl(Integer.parseInt(stringArgument.substring(1)));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(label);
    }
}
