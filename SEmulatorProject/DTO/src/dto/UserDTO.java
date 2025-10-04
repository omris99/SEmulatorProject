package dto;

public class UserDTO {
    private final String userName;
    private final String mainProgramsUploaded;
    private final String functionsContributed;
    private final int creditsUsed;
    private final int creditBalance;
    private final int executionsPerformed;

    public UserDTO(String userName, String mainProgramsUploaded, String functionsContributed, int creditsUsed, int creditBalance, int executionsPerformed) {
        this.userName = userName;
        this.mainProgramsUploaded = mainProgramsUploaded;
        this.functionsContributed = functionsContributed;
        this.creditsUsed = creditsUsed;
        this.creditBalance = creditBalance;
        this.executionsPerformed = executionsPerformed;
    }

    public String getUserName() {
        return userName;
    }
    public String getMainProgramsUploaded() {
        return mainProgramsUploaded;
    }
    public String getFunctionsContributed() {
        return functionsContributed;
    }
    public int getCreditsUsed() {
        return creditsUsed;
    }
    public int getCreditBalance() {
        return creditBalance;
    }
    public int getExecutionsPerformed() {
        return executionsPerformed;
    }
}
