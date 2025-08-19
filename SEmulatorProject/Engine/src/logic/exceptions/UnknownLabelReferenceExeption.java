package logic.exceptions;

public class UnknownLabelReferenceExeption extends RuntimeException {
    private String label;
    public UnknownLabelReferenceExeption(String label) {
        super("There is no instruction labled as: " + label);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
