package serverengine.logic.exceptions;

public class AlreadyExistsProgramException extends RuntimeException {
    private String programName;
    public AlreadyExistsProgramException(String programName) {
        this.programName = programName;
    }

    public String getProgramName() {
        return programName;
    }
}
