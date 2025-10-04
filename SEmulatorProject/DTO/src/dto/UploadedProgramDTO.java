package dto;

public class UploadedProgramDTO {
    private final String uploadedBy;
    private final ProgramDTO program;
    private final int totalExecutions;
    private final int instructionsCount;

    public UploadedProgramDTO(String uploadedBy, ProgramDTO program, int totalExecutions, String contextProgram, int instructionsCount) {
        this.uploadedBy = uploadedBy;
        this.program = program;
        this.totalExecutions = totalExecutions;
        this.instructionsCount = instructionsCount;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public ProgramDTO getProgram() {
        return program;
    }

    public int getTotalExecutions() {
        return totalExecutions;
    }

    public int getInstructionsCount() {
        return instructionsCount;
    }
}
