package serverengine.users;


import clientserverdto.ProgramDTO;
import clientserverdto.UserDTO;
import serverengine.logic.engine.EmulatorEngine;
import serverengine.programs.UploadedProgram;
import serverengine.programs.repo.ProgramsRepo;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String userName;
    private final List<String> mainProgramsUploaded;
    private final List<String> functionsContributed;
    private final EmulatorEngine engine;

    public User(String userName) {
        this.userName = userName;
        this.mainProgramsUploaded = new ArrayList<>();
        this.functionsContributed = new ArrayList<>();
        this.engine = new EmulatorEngine();
    }

    public UserDTO createDTO() {
        return new UserDTO(userName,
                mainProgramsUploaded.size(),
                functionsContributed.size(),
                engine.getCreditsUsed(),
                engine.getCreditsBalance(),
                engine.getExecutionsPerformed());
    }

    private String programsListToString(List<String> programs) {
        if (programs.isEmpty()) {
            return "None";
        }
        return String.join(", ", programs.stream().toList());
    }

    public void addMainProgram(UploadedProgram program) {
        ProgramsRepo programsRepo = ProgramsRepo.getInstance();
        this.mainProgramsUploaded.add(program.getName());

        for (String functionName : program.getProgram().getFunctionsNames()) {
            if (programsRepo.isFunctionUploadedByUser(functionName, this.userName) &&
                    !this.functionsContributed.contains(functionName)) {
                this.functionsContributed.add(functionName);
            }
        }
    }

    public EmulatorEngine getUserEngine() {
        return engine;
    }
}
