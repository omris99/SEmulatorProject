package logic.model.label;

import logic.model.Argument;

public class LabelImpl implements Label, Argument {
    private final String label;

    public LabelImpl(int number) {
        label = "L" + number;
    }

    @Override
    public String getLabelRepresentation() {
        return label;
    }

    @Override
    public String getArgumentString() {
        return getLabelRepresentation();
    }
}
