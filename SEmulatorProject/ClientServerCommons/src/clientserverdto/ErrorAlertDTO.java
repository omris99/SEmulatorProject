package clientserverdto;

import types.errortypes.ExecutionErrorType;

public class ErrorAlertDTO implements DTO{
    private final ExecutionErrorType type;
    private final String title;
    private final String header;
    private final String content;

    public ErrorAlertDTO(ExecutionErrorType type, String title, String header, String content) {
        this.type = type;
        this.title = title;
        this.header = header;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public ExecutionErrorType getType() {
        return type;
    }
}
