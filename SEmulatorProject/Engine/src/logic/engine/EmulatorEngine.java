package logic.engine;

import dto.DTO;
import dto.RunResultsDTO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import logic.exceptions.InvalidXmlFileException;
import logic.exceptions.NumberNotInRangeException;
import logic.exceptions.XmlErrorType;
import logic.execution.ProgramExecutor;
import logic.execution.ProgramExecutorImpl;
import logic.model.argument.Argument;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.generated.SProgram;
import logic.model.mappers.ProgramMapper;
import logic.model.program.Program;
import logic.utils.Utils;

import java.io.File;
import java.util.*;

public class EmulatorEngine implements Engine {
    private Program currentLoadedProgram;
    private Program[] loadedProgramExpendations;
    private final List<RunResultsDTO> history;

    public EmulatorEngine() {
        history = new LinkedList<>();
    }

    @Override
    public void loadProgram(String xmlPath) throws JAXBException, InvalidXmlFileException {
        if(!xmlPath.endsWith(".xml") || !(xmlPath.length() > 4)) {
            throw new InvalidXmlFileException(xmlPath, XmlErrorType.INVALID_EXTENSION);
        }

        File xmlFile = new File(xmlPath);
        if(!xmlFile.exists()) {
            throw new InvalidXmlFileException(xmlPath, XmlErrorType.FILE_MISSING);
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(SProgram.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SProgram sProgram = (SProgram) jaxbUnmarshaller.unmarshal(xmlFile);

        Program loadedProgram = ProgramMapper.toDomain(sProgram);
        Label problemLabel = loadedProgram.validate();
        if (problemLabel != FixedLabel.EMPTY) {
            throw new InvalidXmlFileException(xmlPath, XmlErrorType.UNKNOWN_LABEL,  problemLabel.getRepresentation());
        }

        currentLoadedProgram = loadedProgram;

        //try
        loadedProgramExpendations = new Program[currentLoadedProgram.getMaximalDegree() + 1];
        for (int i = 0; i <= currentLoadedProgram.getMaximalDegree(); i++) {
            loadedProgramExpendations[i] = currentLoadedProgram.getExpandedProgram(i);
        }

        history.clear();
    }

    @Override
    public DTO getLoadedProgramDTO() {
        return currentLoadedProgram.createDTO();
    }

    @Override
    public DTO runLoadedProgram(int degree, String input) {
        ProgramExecutor executor = new ProgramExecutorImpl(currentLoadedProgram.getExpandedProgram(degree));

        Long[] inputs = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toArray(Long[]::new);
        for(Long number : inputs){
            if(number < 0){
                throw new NumberNotInRangeException(Integer.parseInt(number.toString()));
            }
        }

        Map<Variable, Long> userInputToVariablesMap = mapUserInputToVariables(inputs);
        Map<Variable, Long> programUseInitialInputVariablesMap = createProgramUseInitialVariablesMap(inputs);

        Map<Variable, Long> finalVariablesResult = executor.run(new LinkedHashMap<>(programUseInitialInputVariablesMap));

        RunResultsDTO runResults = new RunResultsDTO(
                degree,
                finalVariablesResult.get(Variable.RESULT),
                userInputToVariablesMap,
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.WORK),
                executor.getCyclesCount());
        history.add(runResults);

        return runResults;
    }

    private Set<Variable> getProgramInputVariablesFromOneToN(){
        Set<Variable> inputVariables = new LinkedHashSet<>();

        int maxInputIndex = currentLoadedProgram.getAllInstructionsInputs().stream().map(Argument::getIndex).max(Comparator.naturalOrder()).orElse(0);
        for(int i = 1; i <= maxInputIndex; i++){
            inputVariables.add(new VariableImpl(VariableType.INPUT, i));
        }

        return inputVariables;
    }

    private Map<Variable, Long> mapUserInputToVariables(Long... input){
        int i = 1;
        Map<Variable, Long> userInputToVariablesMap = new LinkedHashMap<>();

        for(long value : input){
            userInputToVariablesMap.put(new VariableImpl(VariableType.INPUT, i), value);
            i++;
        }

        return userInputToVariablesMap;
    }

    private Map<Variable, Long> createProgramUseInitialVariablesMap(Long... inputs){
        Set<Variable> programActualInputVariables = getProgramInputVariablesFromOneToN();

        return Utils.createInputVariablesMap(programActualInputVariables, inputs);
    }

    public DTO getExpandedProgramDTO(int degree){
        return currentLoadedProgram.getExpandedProgram(degree).createDTO();
    }

    @Override
    public List<RunResultsDTO> getHistory() {
        return history;
    }

    public int getMaximalDegree(){
        return currentLoadedProgram.getMaximalDegree();
    }

    public boolean isProgramLoaded() {
        return !(currentLoadedProgram == null);
    }

    public void changeCurrentProgramDegree(int degree) {
        currentLoadedProgram = loadedProgramExpendations[degree];
    }

    @Override
    public void quit(){
        System.exit(0);
    }

}
