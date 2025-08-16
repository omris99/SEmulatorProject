package logic.engine;

import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;

public interface Engine {
    public void loadProgram(String xmlPath) throws FileNotFoundException, JAXBException;
    public void getProgramDetails();
    public long runLoadedProgram(int degree, String input);
    public void getHistory();
}
