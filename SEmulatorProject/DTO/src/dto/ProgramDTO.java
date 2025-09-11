package dto;

import logic.model.instruction.InstructionType;

import java.util.List;
import java.util.Map;

public class ProgramDTO implements DTO{
    private final String name;
    private final List<String> inputNames;
    private final List<String> labelsNames;
    private final List<String> instructionsInDisplayFormat;
    private final List<InstructionDTO> instructionsDTO;
    private final Map<InstructionType, Integer> instructionsTypeCount;
    private final int expandLevelDegree;
    private final int maximalDegree;

    public ProgramDTO(String name, List<String> inputNames, List<String> labelsNames, List<String> instructionsInDisplayFormat, List<InstructionDTO> instructionsDTO, Map<InstructionType, Integer> instructionsTypeCount, int expandLevelDegree, int maximalDegree) {
        this.name = name;
        this.inputNames = inputNames;
        this.labelsNames = labelsNames;
        this.instructionsInDisplayFormat = instructionsInDisplayFormat;
        this.instructionsDTO = instructionsDTO;
        this.instructionsTypeCount = instructionsTypeCount;
        this.expandLevelDegree = expandLevelDegree;
        this.maximalDegree = maximalDegree;
    }

    public String getName() {
        return name;
    }

    public List<String> getInputNames() {
        return inputNames;
    }

    public List<String> getLabelsNames() {
        return labelsNames;
    }

    public List<String> getInstructionsInDisplayFormat() {
        return instructionsInDisplayFormat;
    }

    public List<InstructionDTO> getInstructionsDTO() {
        return instructionsDTO;
    }

    public  Map<InstructionType, Integer> getInstructionsTypeCount() {
        return instructionsTypeCount;
    }

    public int getExpandLevelDegree() {
        return expandLevelDegree;
    }

    public int getMaximalDegree() {
        return maximalDegree;
    }
}
