package logic.engine;

import dto.DTO;
import dto.RunResultsDTO;
import jakarta.xml.bind.JAXBException;
import logic.exceptions.InvalidXmlFileException;
import logic.execution.ExecutionRecord;
import logic.model.argument.variable.Variable;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Engine extends Serializable {
    public void loadProgram(String xmlPath) throws FileNotFoundException, JAXBException, InvalidXmlFileException;
    public DTO getLoadedProgramDTO();
    public DTO runLoadedProgram(int degree, String input);
    public List<ExecutionRecord> getHistory();
    public void quit();
}
