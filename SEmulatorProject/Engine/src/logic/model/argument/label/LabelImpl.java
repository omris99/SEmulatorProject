package logic.model.argument.label;

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
    public int hashCode() {
        return Objects.hashCode(label);
    }
}
