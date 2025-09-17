package logic.model.argument.label;

import logic.model.argument.Argument;

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

        @Override
        public Argument parse(String stringArgument) {
            return null;
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

        @Override
        public Argument parse(String stringArgument) {
            return null;
        }
    };

    @Override
    public abstract String getRepresentation();
}
