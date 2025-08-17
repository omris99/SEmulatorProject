package logic.engine;

import jakarta.xml.bind.JAXBException;
import logic.execution.ExecutionRecord;
import logic.model.argument.variable.Variable;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface Engine {
    public void loadProgram(String xmlPath) throws FileNotFoundException, JAXBException;
    public void getProgramDetails();
    public Map<Variable, Long> runLoadedProgram(int degree, String input);
    public List<ExecutionRecord> getHistory();
    public void quit();
}
