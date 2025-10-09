package exceptions;

public class NumberNotInRangeException extends RuntimeException {
    private final int number;

    public NumberNotInRangeException(int number) {
        super(number + " is not in range");
        this.number = number;
    }

    public int getNumber() {
        return number;
    }


}
