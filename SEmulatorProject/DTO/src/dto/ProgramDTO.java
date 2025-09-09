package dto;

import java.util.List;

public class ProgramDTO implements DTO{
    private final String name;
    private final List<String> inputNames;
    private final List<String> labelsNames;
    private final List<String> instructionsInDisplayFormat;
    private final List<InstructionDTO> instructionsDTO;

    public ProgramDTO(String name, List<String> inputNames, List<String> labelsNames, List<String> instructionsInDisplayFormat, List<InstructionDTO> instructionsDTO) {
        this.name = name;
        this.inputNames = inputNames;
        this.labelsNames = labelsNames;
        this.instructionsInDisplayFormat = instructionsInDisplayFormat;
        this.instructionsDTO = instructionsDTO;
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
}
