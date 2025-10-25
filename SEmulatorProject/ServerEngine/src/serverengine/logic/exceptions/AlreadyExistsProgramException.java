package serverengine.logic.exceptions;

public class AlreadyExistsProgramException extends RuntimeException {
    private final String programName;
    private final boolean isFunction;
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
