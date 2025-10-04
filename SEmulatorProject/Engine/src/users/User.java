package users;

import dto.UserDTO;
import logic.model.program.Function;
import logic.model.program.Program;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private List<Program> mainProgramsUploaded;
    private List<Program> functionsContributed;
    private int creditsUsed;
    private int creditBalance;
    private int executionsPerformed;

    public User(String userName) {
        this.userName = userName;
        this.mainProgramsUploaded = new ArrayList<>();
        this.functionsContributed = new ArrayList<>();
        this.creditsUsed = 0;
        this.creditBalance = 0;
        this.executionsPerformed = 0;
    }

    public UserDTO createDTO() {
        String mainPrograms = programsListToString(this.mainProgramsUploaded);
        String functions = programsListToString(this.functionsContributed);
        return new dto.UserDTO(userName, mainPrograms, functions, creditsUsed, creditBalance, executionsPerformed);
    }

    private String programsListToString(List<Program> programs) {
        if(programs.isEmpty()) {
            return "None";
        }
        return String.join(", ", programs.stream().map(Program::getName).toList());
    }
}
