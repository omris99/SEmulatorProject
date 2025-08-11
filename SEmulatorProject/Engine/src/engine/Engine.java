package engine;

public interface Engine {
    public void loadProgram(String xmlPath);
    public void getProgramDetails();
    public void runLoadedProgram();
    public void getHistory();
}
