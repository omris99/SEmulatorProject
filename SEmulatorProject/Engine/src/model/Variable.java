package model;

public class Variable {
    private final String kind;
    private final int index;

    public Variable(String kind, int index) {
        if (!isStringVariableKindValid(kind)) {
            throw new IllegalArgumentException("Invalid kind: " + kind);
        }
        if (!isStringVariableIndexValid(index)) {
            throw new IllegalArgumentException("Invalid index: " + index);
        }

        this.kind = kind;
        this.index = index;
    }

    public static Variable parse(String stringVariable) {
        if (stringVariable == null || stringVariable.isEmpty()) {
            throw new IllegalArgumentException("Variable string cannot be null or empty");
        }

        String varKind = stringVariable.substring(0, 1);
        if (!isStringVariableKindValid(varKind)) {
            throw new IllegalArgumentException("Invalid variable kind: " + varKind);
        }

        if (varKind.equals("y")) {
            if (stringVariable.length() != 1) {
                throw new IllegalArgumentException("Variable of kind 'y' must not have an index");
            }
            return new Variable(varKind, -1);
        }

        if (stringVariable.length() < 2) {
            throw new IllegalArgumentException("Variable missing index");
        }

        int varIndex;

        try {
            varIndex = Integer.parseInt(stringVariable.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid index format in variable: " + stringVariable, e);
        }

        if (!isStringVariableIndexValid(varIndex)) {
            throw new IllegalArgumentException("Invalid index");
        }

        return new Variable(varKind, varIndex);
    }

    private static boolean isStringVariableKindValid(String kind) {
        return kind.equals("x") || kind.equals("y") || kind.equals("z");
    }

    private static boolean isStringVariableIndexValid(int index) {
        return index >= 0; // או כל כלל אחר שאתה רוצה
    }

    public String getKind() {
        return kind;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        if (kind.equals("y")) {
            return kind;
        } else {
            return kind + index;
        }
    }

}
