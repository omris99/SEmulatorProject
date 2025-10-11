package types.errortypes;

public enum XmlErrorType implements ErrorType {
    FILE_MISSING("File not found"),
    INVALID_EXTENSION("Invalid file type, must be .xml"),
    UNKNOWN_LABEL("Unknown label");

    private final String userString;

    XmlErrorType(String userString) {
        this.userString = userString;
    }

    @Override
    public String getUserString() {
        return userString;
    }
}
