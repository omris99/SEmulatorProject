package exceptions;

import types.errortypes.ArgumentErrorType;

public class InvalidArgumentException extends RuntimeException {
  private final String argumentName;
  private final ArgumentErrorType errorType;

    public InvalidArgumentException(String argumentName, ArgumentErrorType errorType) {
        super("Invalid argument");
        this.argumentName = argumentName;
        this.errorType = errorType;
    }

  public String getArgumentName() {
    return argumentName;
  }

  public ArgumentErrorType getErrorType() {
      return errorType;
  }
}
