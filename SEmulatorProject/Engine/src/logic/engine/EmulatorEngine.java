package logic.engine;


import dto.DTO;
import logic.exceptions.InvalidXmlFileException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import logic.execution.ExecutionRecord;
import logic.execution.ProgramExecutor;
import logic.execution.ProgramExecutorImpl;
import logic.model.argument.variable.Variable;
import logic.model.instruction.Instruction;
import logic.model.program.Program;
import logic.model.generated.SProgram;
import logic.model.mappers.ProgramMapper;
import logic.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EmulatorEngine implements Engine {
    Program program;
    ProgramExecutor executor;
    List<ExecutionRecord> history;

    public EmulatorEngine() {
        history = new LinkedList<>();
    }
    public String getProgramName() {
        return program.getName();
    }


    public List<Instruction> getInstructions() {
        return program.getInstructions();
    }

    @Override
    public void loadProgram(String xmlPath) throws FileNotFoundException, JAXBException {
        if(!xmlPath.endsWith(".xml") || !(xmlPath.length() > 4)) {
            throw new InvalidXmlFileException(xmlPath);
        }

        File xmlFile = new File(xmlPath);
        if(!xmlFile.exists()) {
            throw new FileNotFoundException("File " + xmlPath + " does not exist.");
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(SProgram.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SProgram sProgram = (SProgram) jaxbUnmarshaller.unmarshal(xmlFile);
        Program loadedProgram = ProgramMapper.toDomain(sProgram);
        loadedProgram.validate();
        program = loadedProgram;
//        System.out.println(sProgram);
    }

    @Override
    public DTO getLoadedProgramDTO() {
        return program.createDTO();
    }

    @Override
    public Map<Variable, Long> runLoadedProgram(int degree, String input) {
        executor = new ProgramExecutorImpl(program.expand(degree));

        Long[] inputs = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toArray(Long[]::new);

        getExpandedProgramDTO(degree);
        Map<Variable, Long> finalVariablesResult = executor.run(inputs);


        ExecutionRecord record = new ExecutionRecord(degree,
                Utils.createInputVariablesMap(program.getAllInstructionsInputs(), inputs),
                finalVariablesResult.get(Variable.RESULT),
                executor.getCyclesCount());
        history.add(record);

        return finalVariablesResult;
    }

    public DTO getExpandedProgramDTO(int degree){
        return program.expand(degree).createDTO();
    }

    @Override
    public List<ExecutionRecord> getHistory() {
        return history;

    }

    public int getLastExecutionCycles() {
        return executor.getCyclesCount();
    }

    public int getMaximalDegree(){
        return program.getMaximalDegree();
    }

    public boolean isProgramLoaded() {
        return !(program == null);
    }

    @Override
    public void quit(){
        System.exit(0);
    }
}
