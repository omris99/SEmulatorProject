package logic.exceptions;

public class InvalidArgumentException extends RuntimeException {
  private String argumentName;
  private XmlErrorType errorType;

    public InvalidArgumentException(String argumentName, XmlErrorType errorType) {
        super("Invalid argument");
        this.argumentName = argumentName;
        this.errorType = errorType;
    }

  public String getArgumentName() {
    return argumentName;
  }

  public XmlErrorType getErrorType() {
      return errorType;
  }
}
