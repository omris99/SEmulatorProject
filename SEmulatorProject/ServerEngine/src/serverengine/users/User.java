package serverengine.users;


import clientserverdto.ProgramDTO;
import clientserverdto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private List<String> mainProgramsUploaded;
    private List<String> functionsContributed;
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
        return new UserDTO(userName, mainPrograms, functions, creditsUsed, creditBalance, executionsPerformed);
    }

    private String programsListToString(List<String> programs) {
        if(programs.isEmpty()) {
            return "None";
        }
        return String.join(", ", programs.stream().toList());
    }

    public void addMainProgram(ProgramDTO program) {
        System.out.println("Adding main program: " + program.getName() + " to user: " + this.userName);
        this.mainProgramsUploaded.add(program.getName());
        this.functionsContributed.addAll(program.getFunctionsNames());
        System.out.println("Adding main program: " + program.getName() + " to user: " + this.userName);

    }
}
