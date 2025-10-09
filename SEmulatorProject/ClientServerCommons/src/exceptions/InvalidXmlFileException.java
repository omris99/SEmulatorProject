package exceptions;

import types.errortypes.XmlErrorType;

public class InvalidXmlFileException extends Exception {
    private final XmlErrorType errorType;
    private final String filePath;
    private final String element;

    public InvalidXmlFileException(String filePath, XmlErrorType errorType) {
        this(filePath, errorType, "");
    }

    public InvalidXmlFileException(String Path, XmlErrorType errorType, String element) {
        super("Invalid XML File: " + Path + "| Type: " + errorType);
        this.filePath = Path;
        this.element = element;
        this.errorType = errorType;
    }

    public XmlErrorType getType() {
        return errorType;
    }
    public String getFilePath() {
        return filePath;
    }
    public String getElement() {
        return element;
    }

}
