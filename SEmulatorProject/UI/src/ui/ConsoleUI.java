package ui;

import logic.engine.EmulatorEngine;
import logic.exceptions.InvalidXmlFileException;
import logic.exceptions.UnknownLabelReferenceExeption;
import jakarta.xml.bind.JAXBException;
import logic.model.instruction.Instruction;
import logic.model.variable.Variable;

import java.io.FileNotFoundException;
/*
 * TODO:
 *  1. search for xml application errors and make sure the errors exceptions corrrectly
 *  2. ensure that every exception message is fully detailed.
 *  3. improve the implemetntation of VariableImpl class (parse)
 */

public class ConsoleUI implements UI
{
    EmulatorEngine engine;

    public ConsoleUI()
    {
        engine = new EmulatorEngine();
    }

    public static void main(String[] args) {
//        List<SInstruction> instructions = new ArrayList<SInstruction>();
//        SInstruction instruction1 = new SInstruction(
//                InstructionType.basic,
//                InstructionName.DECREASE,
//                new SVariable("x",1),
//                new );
//        SProgram program = new SProgram("Sanity-basic", instructions);
        UI ui = new ConsoleUI();
        ui.run();
    }

    @Override
    public void loadProgram() {
        //needs to ask for path from user and than send it to engine...now its only example.
        String xmlPath = "/Users/omrishtruzer/Documents/SEmulatorProject/Test XMLFiles/synthetic.xml";

        try{
            engine.loadProgram(xmlPath);
            System.out.println("XML FILE: " + xmlPath + " Loaded successfully.");
        } catch (InvalidXmlFileException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (JAXBException e) {
            System.out.println("Error reading XML: " + e.getMessage());
        } catch (UnknownLabelReferenceExeption e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void showProgramDetails() {
        int i = 1;

        System.out.println(String.format("Program Name: %s", engine.getProgramName()));
        System.out.println(String.format("Inputs Names: %s", engine.getProgramInputsNames()));

        System.out.println(String.format("Labels Names: %s", engine.getProgramLabelsNames()));
        for(Instruction instruction : engine.getInstructions())
        {
            System.out.print(String.format("#%d ", i));
            System.out.println(instruction.getInstructionDisplayFormat());
            i++;
        }
    }

    @Override
    public void expand() {

    }

    @Override
    public void runLoadedProgram() {

    }

    @Override
    public void run() {
        loadProgram();
        showProgramDetails();
    }

    @Override
    public void showHistory() {

    }

    @Override
    public void quitProgram() {

    }
}
