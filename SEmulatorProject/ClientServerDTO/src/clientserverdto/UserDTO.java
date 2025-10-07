package clientserverdto;

import java.util.Objects;

public class UserDTO {
    private final String userName;
    private final String mainProgramsUploaded;
    private final String functionsContributed;
    private final long creditsUsed;
    private final long creditBalance;
    private final int executionsPerformed;

    public UserDTO(String userName, String mainProgramsUploaded, String functionsContributed, long creditsUsed, long creditBalance, int executionsPerformed) {
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
    public long getCreditsUsed() {
        return creditsUsed;
    }
    public long getCreditBalance() {
        return creditBalance;
    }
    public int getExecutionsPerformed() {
        return executionsPerformed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;

        return Objects.equals(userName, userDTO.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, mainProgramsUploaded, functionsContributed, creditsUsed, creditBalance, executionsPerformed);
    }
}
