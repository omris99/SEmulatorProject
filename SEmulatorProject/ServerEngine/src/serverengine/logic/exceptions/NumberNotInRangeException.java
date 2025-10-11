package serverengine.logic.exceptions;

public class NumberNotInRangeException extends RuntimeException {
    private final long number;

    public NumberNotInRangeException(long number) {
        super(number + " is not in range");
        this.number = number;
    }

    public long getNumber() {
        return number;
    }


}
