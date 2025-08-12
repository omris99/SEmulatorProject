package model;

import java.util.Objects;

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
        if (stringVariable == null) {
            throw new IllegalArgumentException("Variable string cannot be null or empty");
        }
        else if (stringVariable.isEmpty()) {
            return new Variable("", 0);
        }

        String varKind = stringVariable.substring(0, 1);
        if (!isStringVariableKindValid(varKind)) {
            throw new IllegalArgumentException("Invalid variable kind: " + varKind);
        }

        if (varKind.equals("y")) {
            if (stringVariable.length() != 1) {
                throw new IllegalArgumentException("Variable of kind 'y' must not have an index");
            }
            return new Variable(varKind, 0);
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
        return index >= 0;
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

    public boolean isInputVariable(){
        return kind.equals("x");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return index == variable.index && Objects.equals(kind, variable.kind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, index);
    }
}
