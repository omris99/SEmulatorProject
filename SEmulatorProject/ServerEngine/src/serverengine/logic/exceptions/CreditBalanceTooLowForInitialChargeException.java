package serverengine.logic.exceptions;

public class CreditBalanceTooLowForInitialChargeException extends CreditBalanceTooLowException {
    private final long architectureCost;
    private final long averageProgramCost;

    public CreditBalanceTooLowForInitialChargeException(long creditsCost, long creditsBalance, long architectureCost, long averageProgramCost) {
        super(creditsCost, creditsBalance);
        this.architectureCost = architectureCost;
        this.averageProgramCost = averageProgramCost;
    }

    public long getArchitectureCost() {
        return architectureCost;
    }

    public long getAverageProgramCost() {
        return averageProgramCost;
    }
}
