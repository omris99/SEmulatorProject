package logic.model.argument.label;

public enum FixedLabel implements Label {
    EXIT {
        @Override
        public String getRepresentation() {
            return "EXIT";
        }
    },
    EMPTY {
        @Override
        public String getRepresentation() {
            return "";
        }
    };

    @Override
    public abstract String getRepresentation();
}
