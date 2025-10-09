package exceptions;

public class AlreadyExistsProgramException extends RuntimeException {
    private String programName;
    private boolean isFunction;
    public AlreadyExistsProgramException(String programName, boolean isFunction) {
        this.programName = programName;
        this.isFunction = isFunction;
    }

    public String getProgramName() {
        return programName;
    }

    public boolean isFunction() {
        return isFunction;
    }
}
