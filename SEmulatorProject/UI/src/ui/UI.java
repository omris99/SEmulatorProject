package ui;

import dto.ProgramDTO;

public interface UI {
    public void showMainMenuAndExecuteUserChoice();
    public void loadProgram();
    public void showProgramDetails(ProgramDTO programDetails);
    public void expand();
    public void runLoadedProgram();
    public void showHistory();
    public void quitProgram();
    public void run();
}
