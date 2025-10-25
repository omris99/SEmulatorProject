package serverengine.logic.engine;

import clientserverdto.DTO;
import clientserverdto.ExecutionHistoryDTO;
import java.io.Serializable;
import java.util.List;


public interface Engine extends Serializable {
    DTO getLoadedProgramDTO();
    List<ExecutionHistoryDTO> getHistory();
}
