package logic.engine;

import dto.DTO;
import logic.exceptions.InvalidXmlFileException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import logic.exceptions.NumberNotInRangeException;
import logic.exceptions.XmlErrorType;
import logic.execution.ExecutionRecord;
import logic.execution.ProgramExecutor;
import logic.execution.ProgramExecutorImpl;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.program.Program;
import logic.model.generated.SProgram;
import logic.model.mappers.ProgramMapper;
import logic.utils.Utils;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/*
* TODO:
*  1. CREATE A RunResultsDTO
*
* */

public class EmulatorEngine implements Engine {
    private Program currentLoadedProgram;
    private transient ProgramExecutor executor;
    private final List<ExecutionRecord> history;

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
            throw new InvalidXmlFileException(xmlPath, XmlErrorType.INVALID_ELEMENT,  problemLabel.getRepresentation());
        }

        currentLoadedProgram = loadedProgram;
        history.clear();
    }

    @Override
    public DTO getLoadedProgramDTO() {
        return currentLoadedProgram.createDTO();
    }

    @Override
    public Map<Variable, Long> runLoadedProgram(int degree, String input) {
        executor = new ProgramExecutorImpl(currentLoadedProgram.getExpandedProgram(degree));

        Long[] inputs = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toArray(Long[]::new);
        for(Long number : inputs){
            if(number < 0){
                throw new NumberNotInRangeException(Integer.parseInt(number.toString()));
            }
        }

//        getExpandedProgramDTO(degree);
        Map<Variable, Long> finalVariablesResult = executor.run(inputs);


        ExecutionRecord record = new ExecutionRecord(degree,
                Utils.createInputVariablesMap(currentLoadedProgram.getAllInstructionsInputs(), inputs),
                finalVariablesResult.get(Variable.RESULT),
                executor.getCyclesCount());
        history.add(record);

        return finalVariablesResult;
    }

    public DTO getExpandedProgramDTO(int degree){
        return currentLoadedProgram.getExpandedProgram(degree).createDTO();
    }

    @Override
    public List<ExecutionRecord> getHistory() {
        return history;
    }

    public int getLastExecutionCycles() {
        return executor.getCyclesCount();
    }

    public int getMaximalDegree(){
        return currentLoadedProgram.getMaximalDegree();
    }

    public boolean isProgramLoaded() {
        return !(currentLoadedProgram == null);
    }

    @Override
    public void quit(){
        System.exit(0);
    }

}
