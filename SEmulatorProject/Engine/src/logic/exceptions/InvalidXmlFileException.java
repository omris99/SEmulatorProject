package logic.exceptions;

public class InvalidXmlFileException extends RuntimeException {
    public InvalidXmlFileException(String Path) {
        super("Invalid file type: " + Path + " must be .xml");
    }
}
