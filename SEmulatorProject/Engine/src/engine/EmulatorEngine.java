package engine;


import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import model.Instruction;
import model.Program;
import model.generated.SProgram;
import model.mappers.ProgramMapper;

import java.io.File;
import java.util.List;

public class EmulatorEngine implements Engine {
    Program program;

    public String getProgramName() {
        return program.getName();
    }

    public List<String> getProgramLabelsNames() {
        return program.getAllLabels();
    }
    public List<String> getProgramInputsNames() {
        return program.getAllInputsNames();
    }

    public List<Instruction> getInstructions() {
        return program.getInstructions();
    }

    @Override
    public void loadProgram(String xmlPath) {
        try{
            File xmlFile = new File(xmlPath);
            JAXBContext jaxbContext = JAXBContext.newInstance(SProgram.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SProgram sProgram = (SProgram) jaxbUnmarshaller.unmarshal(xmlFile);
            program = ProgramMapper.toDomain(sProgram);
            System.out.println(sProgram);
        }
        catch (JAXBException e){
            e.printStackTrace();
        }

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
