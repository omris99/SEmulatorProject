package serverengine.programs;

import clientserverdto.ProgramDTO;
import clientserverdto.UploadedProgramDTO;
import serverengine.logic.model.program.Program;

public class UploadedProgram {
    private final String uploadedBy;
    private final Program program;
    private int totalExecutions;
    private final String contextProgram;
    private final int instructionsCount;
    private long averageCyclesPerExecution;

    public UploadedProgram(String uploadedBy, Program program, String contextProgram) {
        this.uploadedBy = uploadedBy;
        this.program = program;
        this.totalExecutions = 0;
        this.contextProgram = contextProgram;
        this.instructionsCount = program.getInstructions().size();
        this.averageCyclesPerExecution = 0;
    }

    public UploadedProgramDTO createDTO() {
        return new UploadedProgramDTO(
                uploadedBy,
                (ProgramDTO) program.createDTO(),
                totalExecutions,
                contextProgram,
                instructionsCount,
                averageCyclesPerExecution
        );
    }

    public String getName(){
        return program.getName();
    }

    public String getUserString(){
        return program.getRepresentation();
    }

    public Program getProgram(){
        return program;
    }

    private void incrementTotalExecutions(){
        totalExecutions++;
    }

    private void updateAverageCyclesPerExecution(long cycles){
        if(totalExecutions == 1){
            averageCyclesPerExecution = cycles;
        } else {
            averageCyclesPerExecution = (averageCyclesPerExecution * (totalExecutions - 1) + cycles) / totalExecutions;
        }
    }

    public void updateDataAfterExecution(long cycles){
        incrementTotalExecutions();
        updateAverageCyclesPerExecution(cycles);
    }

    public long getAverageCyclesPerExecution(){
        return averageCyclesPerExecution;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }
}
