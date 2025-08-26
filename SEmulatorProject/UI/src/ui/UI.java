package ui;

import dto.ProgramDTO;

public interface UI {
    void showMainMenuAndExecuteUserChoice();
    void loadProgram();
    void showProgramDetails(ProgramDTO programDetails);
    void expand();
    void runLoadedProgram();
    void showHistory();
    void quitProgram();
    void run();
}
