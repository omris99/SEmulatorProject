package exceptions;

public class CreditBalanceTooLowException extends Exception {
    private final long creditsCost;
    private final long creditsBalance;

    public CreditBalanceTooLowException(long creditsCost, long creditsBalance) {
        this.creditsCost = creditsCost;
        this.creditsBalance = creditsBalance;
    }

    public  long getCreditsCost() {
        return creditsCost;
    }

    public long getCreditsBalance() {
        return creditsBalance;
    }
}
