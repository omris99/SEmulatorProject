package serverengine.logic.model.argument.label;

public enum FixedLabel implements Label {
    EXIT {
        @Override
        public String getRepresentation() {
            return "EXIT";
        }

        @Override
        public int getIndex() {
            return 0;
        }

    },
    EMPTY {
        @Override
        public String getRepresentation() {
            return "";
        }

        @Override
        public int getIndex() {
            return 0;
        }

    };

    @Override
    public abstract String getRepresentation();
}
