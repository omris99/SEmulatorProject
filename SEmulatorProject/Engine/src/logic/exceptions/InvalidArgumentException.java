package logic.exceptions;

public class InvalidArgumentException extends RuntimeException {
  private String argumentName;
  private ArgumentErrorType errorType;

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
