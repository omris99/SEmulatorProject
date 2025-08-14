package logic.engine;


import logic.exceptions.InvalidXmlFileException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import logic.model.instruction.Instruction;
import logic.model.instruction.InstructionOld;
import logic.model.program.Program;
import logic.model.program.ProgramImpl;
import logic.model.variable.Variable;
import logic.model.generated.SProgram;
import logic.model.mappers.ProgramMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

public class EmulatorEngine implements Engine {
    Program program;

    public String getProgramName() {
        return program.getName();
    }

    public Set<String> getProgramLabelsNames() {
        return program.getAllInstructionsLabels();
    }
    public Set<Variable> getProgramInputsNames() {
        return program.getAllInputsNames();
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
        program = ProgramMapper.toDomain(sProgram);
//        System.out.println(sProgram);
    }

    @Override
    public void getProgramDetails() {

    }

    @Override
    public void runLoadedProgram() {

    }

    @Override
    public void getHistory() {

    }

}
