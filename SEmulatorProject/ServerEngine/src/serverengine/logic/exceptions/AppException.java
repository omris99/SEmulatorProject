package serverengine.logic.exceptions;

import types.errortypes.ErrorType;

public class AppException extends RuntimeException{
    private final ErrorType errorType;
    private final String details;

    public AppException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.details = "";
    }

    public AppException(ErrorType errorType, String message, String details) {
        super(message);
        this.errorType = errorType;
        this.details = details;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getDetails() {
        return details;
    }
}
