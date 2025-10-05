package serverengine.logic.model.functionsrepo;

import clientserverdto.ProgramDTO;
import clientserverdto.UploadedProgramDTO;
import serverengine.logic.model.program.Program;

public class UploadedProgram {
    private final String uploadedBy;
    private final Program program;
    private int totalExecutions;
    private final String contextProgram;
    private final int instructionsCount;

    public UploadedProgram(String uploadedBy, Program program, String contextProgram) {
        this.uploadedBy = uploadedBy;
        this.program = program;
        this.totalExecutions = 0;
        this.contextProgram = contextProgram;
        this.instructionsCount = program.getInstructions().size();
    }

    public UploadedProgramDTO createDTO() {
        return new UploadedProgramDTO(
                uploadedBy,
                (ProgramDTO) program.createDTO(),
                totalExecutions,
                contextProgram,
                instructionsCount
        );
    }

    public String getName(){
        return program.getName();
    }

    public Program getProgram(){
        return program;
    }
}
