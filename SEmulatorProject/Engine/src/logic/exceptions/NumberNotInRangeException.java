package logic.exceptions;

public class NumberNotInRangeException extends RuntimeException {
    public NumberNotInRangeException(int number) {
        super(number + " is not in range");
    }
}
