package serverengine.logic.utils;

import clientserverdto.ErrorDTO;
import serverengine.logic.exceptions.CreditBalanceTooLowException;
import serverengine.logic.exceptions.CreditBalanceTooLowForInitialChargeException;
import serverengine.logic.exceptions.InvalidArchitectureException;
import serverengine.logic.exceptions.NumberNotInRangeException;
import types.errortypes.ExecutionErrorType;

public class ErrorMapper {

    public static ErrorDTO fromException(Exception e) {
        if (e instanceof NumberFormatException) {
            return new ErrorDTO(
                    ExecutionErrorType.BAD_INPUT_VARIABLES,
                    "Error Starting Execution",
                    "Invalid Input",
                    "The input is invalid. Please enter integers only."
            );
        }

        if (e instanceof NumberNotInRangeException) {
            NumberNotInRangeException ex = (NumberNotInRangeException) e;
            return new ErrorDTO(
                    ExecutionErrorType.BAD_INPUT_VARIABLES,
                    "Error Starting Execution",
                    "Negative Number Submitted",
                    "You entered the number: " + ex.getNumber() + " which is not positive.\n" +
                            "Please enter only Positive Numbers."
            );
        }

        if (e instanceof InvalidArchitectureException) {
            InvalidArchitectureException ex = (InvalidArchitectureException) e;
            return new ErrorDTO(
                    ExecutionErrorType.UNCOMPATIBLE_ARCHITECTURE,
                    "Error Starting Execution",
                    "Invalid Architecture Selected",
                    "Minimum architecture required for this program is: " + ex.getMinimumArchitecture() + ".\n" +
                            "You selected: " + ex.getSelectedArchitecture() + ".\n" +
                            "Please select a valid architecture and try again."
            );
        }

        if (e instanceof CreditBalanceTooLowForInitialChargeException) {
            CreditBalanceTooLowForInitialChargeException ex = (CreditBalanceTooLowForInitialChargeException) e;
            String message = String.format(
                    "Your credit balance is too low to run this program.%n%n" +
                            "• Architecture cost: %d%n" +
                            "• Average program cost: %d%n" +
                            "• Minimum Balance Required: %d%n" +
                            "• Your balance: %d",
                    ex.getArchitectureCost(),
                    ex.getAverageProgramCost(),
                    ex.getCreditsCost(),
                    ex.getCreditsBalance()
            );
            return new ErrorDTO(
                    ExecutionErrorType.CREDIT_BALANCE_TOO_LOW,
                    "Credit Balance Too Low",
                    "Can't Start Program run",
                    message
            );
        }

        if (e instanceof CreditBalanceTooLowException) {
            CreditBalanceTooLowException ex = (CreditBalanceTooLowException) e;
            return new ErrorDTO(
                    ExecutionErrorType.CREDIT_BALANCE_TOO_LOW,
                    "Credit Balance Too Low",
                    "Can't Run Program",
                    "Credit Balance Too Low. Cost of current instruction: " + ex.getCreditsCost() +
                            ", Your Balance: " + ex.getCreditsBalance()
            );
        }

        return new ErrorDTO(
                ExecutionErrorType.UNKNOWN,
                "Unexpected Error",
                "Something went wrong",
                e.getMessage() != null ? e.getMessage() : "Unknown error occurred."
        );
    }
}
