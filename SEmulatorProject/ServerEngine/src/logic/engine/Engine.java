package logic.engine;

import dto.DTO;
import dto.RunResultsDTO;
import jakarta.xml.bind.JAXBException;
import logic.exceptions.InvalidXmlFileException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;


public interface Engine extends Serializable {
    void loadProgram(String xmlPath) throws FileNotFoundException, JAXBException, InvalidXmlFileException;
    DTO getLoadedProgramDTO();
    DTO runLoadedProgramWithCommaSeperatedInput(int degree, String input);
    List<RunResultsDTO> getHistory();
    void quit();
}
