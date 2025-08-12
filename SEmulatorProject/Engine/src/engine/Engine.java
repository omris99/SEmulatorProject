package engine;

import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;

public interface Engine {
    public void loadProgram(String xmlPath) throws FileNotFoundException, JAXBException;
    public void getProgramDetails();
    public void runLoadedProgram();
    public void getHistory();
}
