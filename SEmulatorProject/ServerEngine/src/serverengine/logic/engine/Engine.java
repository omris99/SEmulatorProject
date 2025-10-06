package serverengine.logic.engine;

import clientserverdto.DTO;
import clientserverdto.ExecutionHistoryDTO;
import clientserverdto.RunResultsDTO;
import jakarta.xml.bind.JAXBException;
import serverengine.logic.exceptions.InvalidXmlFileException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;


public interface Engine extends Serializable {
    DTO getLoadedProgramDTO();
    List<ExecutionHistoryDTO> getHistory();
    void quit();
}
