package types.errortypes;

public enum ExecutionErrorType implements ErrorType {
    CREDIT_BALANCE_TOO_LOW("Credit balance too low"),
    UNCOMPATIBLE_ARCHITECTURE("Incompatible architecture"),
    BAD_INPUT_VARIABLES("Bad input variables"),
    INVALID_CREDITS_AMOUNT("Invalid credits amount"),
    UNKNOWN("Unknown error");

    private final String userString;

    ExecutionErrorType(String userString) {
        this.userString = userString;
    }


    @Override
    public String getUserString() {
        return userString;
    }
}
