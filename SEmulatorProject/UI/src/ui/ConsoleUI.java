package ui;

import logic.engine.EmulatorEngine;
import logic.exceptions.InvalidXmlFileException;
import logic.exceptions.UnknownLabelReferenceExeption;
import jakarta.xml.bind.JAXBException;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableType;
import logic.model.instruction.Instruction;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
/*
 * TODO:
 *  1. search for xml application errors and make sure the errors exceptions corrrectly
 *  2. ensure that every exception message is fully detailed.
 *  3. improve the implemetntation of VariableImpl class (parse)
 */

public class ConsoleUI implements UI
{
    EmulatorEngine engine;
    Scanner inputScanner;

    public ConsoleUI()
    {
        engine = new EmulatorEngine();
        inputScanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        UI ui = new ConsoleUI();
        ui.run();
    }

    @Override
    public void loadProgram() {
        //needs to ask for path from user and than send it to engine...now its only example.
        String xmlPath = "/Users/omrishtruzer/Documents/SEmulatorProject/Test XMLFiles/badic.xml";

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
//        int maximalDegree = engine.getProgramMaximalDegree();
//        System.out.println("Maximal Degree: ", maximalDegree);
//        System.out.println("Please Enter Desired running degree: ");
//        int runningDegree = inputScanner.nextInt();

        System.out.print("Available program inputs: ");
        System.out.println(String.format("%s", engine.getProgramInputsNames()));
        System.out.println("Enter input values separated by commas (e.g: 5,10,15): ");
        String inputs = inputScanner.nextLine();
        System.out.println("\nRunning program with the following inputs: " + inputs + "\n");
        showProgramRunResults(engine.runLoadedProgram(0, inputs));

//        System.out.println(String.format("The Result is: y = %d", engine.runLoadedProgram(0, inputs)));
    }

    @Override
    public void run() {
        loadProgram();
        showProgramDetails();
        runLoadedProgram();
    }

    @Override
    public void showHistory() {


    }

    @Override
    public void quitProgram() {

    }

    private void showProgramRunResults(Map<Variable, Long> results) {
        long yValue = 0L;
        Map<Variable, Long> inputVariables = new LinkedHashMap<>();
        Map<Variable, Long> workVariables = new LinkedHashMap<>();

        for(Map.Entry<Variable, Long> entry : results.entrySet()){
            switch(entry.getKey().getType()){
                case INPUT:
                    inputVariables.put(entry.getKey(), entry.getValue());
                    break;
                case WORK:
                    workVariables.put(entry.getKey(), entry.getValue());
                    break;
                case RESULT:
                    yValue = entry.getValue();
                    break;
            }
        }

        inputVariables = sortVariablesByTheirNumber(inputVariables);
        workVariables = sortVariablesByTheirNumber(workVariables);
        System.out.println("**********************");
        System.out.println("Program Run Results:");
        System.out.println("----------------------");
        System.out.println(String.format("y = %d ", yValue));

        for(Variable variable : inputVariables.keySet()){
            System.out.println(String.format("%s = %d ", variable.getRepresentation(), inputVariables.get(variable)));
        }

        for(Variable variable : workVariables.keySet()){
            System.out.println(String.format("%s = %d ", variable.getRepresentation(), workVariables.get(variable)));
        }

        System.out.println(String.format("Cycles Count = %d ", engine.getLastExecutionCycles()));

        System.out.println("**********************");
    }

    private Map<Variable, Long> sortVariablesByTheirNumber(Map<Variable, Long> variables) {
        Map<Variable, Long> sortedVariables = variables.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(variable -> variable.getKey().getNumber()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return sortedVariables;
    }
}
