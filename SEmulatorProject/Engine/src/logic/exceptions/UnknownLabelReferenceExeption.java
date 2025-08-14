package logic.exceptions;

public class UnknownLabelReferenceExeption extends RuntimeException {
    public UnknownLabelReferenceExeption(String label) {
        super("There is no instruction labled as: " + label);
    }
}
