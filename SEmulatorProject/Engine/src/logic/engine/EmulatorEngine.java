package logic.engine;


import logic.exceptions.InvalidXmlFileException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import logic.execution.ProgramExecutor;
import logic.execution.ProgramExecutorImpl;
import logic.model.argument.Argument;
import logic.model.instruction.Instruction;
import logic.model.argument.label.FixedLabel;
import logic.model.program.Program;
import logic.model.generated.SProgram;
import logic.model.mappers.ProgramMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class EmulatorEngine implements Engine {
    Program program;

    public String getProgramName() {
        return program.getName();
    }

    public List<String> getProgramLabelsNames() {
        List<String> programLabelsNames = program.getAllInstructionsLabels().stream()
                .filter(label -> !label.equals(FixedLabel.EXIT))
                .map(Argument::getRepresentation)
                .collect(Collectors.toList());

        if (program.getAllInstructionsLabels().contains(FixedLabel.EXIT)) {
            programLabelsNames.add(FixedLabel.EXIT.getRepresentation());
        }

        return programLabelsNames;
    }
    public List<String> getProgramInputsNames() {
        return program.getAllInstructionsInputs().stream().map(Argument::getRepresentation).collect(Collectors.toList());
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
    public void getProgramDetails() {

    }

    @Override
    public void runLoadedProgram(int degree, String input) {
        ProgramExecutor executor = new ProgramExecutorImpl(program);


    }

    @Override
    public void getHistory() {

    }

}
