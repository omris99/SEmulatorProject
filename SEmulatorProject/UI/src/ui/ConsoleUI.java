package ui;

import logic.engine.EmulatorEngine;
import logic.exceptions.InvalidXmlFileException;
import logic.exceptions.NumberNotInRangeException;
import logic.exceptions.UnknownLabelReferenceExeption;
import jakarta.xml.bind.JAXBException;
import logic.execution.ExecutionRecord;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableType;
import logic.model.instruction.Instruction;
import logic.utils.Utils;
import ui.menu.MainMenu;
import ui.menu.Menu;
import ui.menu.option.MainMenuOption;
import ui.menu.option.MenuOption;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
/*
 * TODO:
 *  1. search for xml application errors and make sure the errors exceptions corrrectly
 *  2. ensure that every exception message is fully detailed.
 *  3. improve the implemetntation of VariableImpl class (parse)
 *  4. handle if user type bigger degree than maximal in expand
 */

public class ConsoleUI implements UI {
    EmulatorEngine engine;
    Scanner inputScanner;
    Menu mainMenu;

    public ConsoleUI() {
        engine = new EmulatorEngine();
        inputScanner = new Scanner(System.in);
        mainMenu = buildMainMenu();
    }

    public static void main(String[] args) {
        UI ui = new ConsoleUI();
        ui.run();
    }

    private Menu buildMainMenu()
    {
        List<MenuOption> options = new ArrayList<>();
        options.addAll(Arrays.asList(MainMenuOption.values()));
        return new MainMenu(options);
    }

    @Override
    public void showMainMenuAndExecuteUserChoice() {
        MenuOption choice = mainMenu.printAndReturnUserChoice();
        executeMainMenuOption(choice);
    }

    private void executeMainMenuOption(MenuOption option) {
        boolean isProgramLoaded = engine.isProgramLoaded();
        if(!(isProgramLoaded || option.equals(MainMenuOption.LOAD_PROGRAM))) {
            System.out.println("Error: Program Not Loaded yet.");
        }
        else{
            switch (option) {
                case MainMenuOption.LOAD_PROGRAM -> loadProgram();
                case MainMenuOption.SHOW_PROGRAM -> showProgramDetails();
                case MainMenuOption.EXPAND_PROGRAM -> expand();
                case MainMenuOption.RUN_PROGRAM -> runLoadedProgram();
                case MainMenuOption.SHOW_HISTORY ->  showHistory();
                case MainMenuOption.EXIT_PROGRAM -> quitProgram();
                default -> throw new IllegalStateException("Unexpected value: " + option);
            }
        }
    }

    @Override
    public void loadProgram() {
        //needs to ask for path from user and than send it to engine...now its only example.
        String xmlPath = "/Users/omrishtruzer/Documents/SEmulatorProject/Test XMLFiles/synthetic.xml";

        try {
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
        for (Instruction instruction : engine.getInstructions()) {
            System.out.print(String.format("#%d ", i));
            System.out.println(instruction.getInstructionDisplayFormat());
            i++;
        }

    }

    @Override
    public void expand() {
        int expansionDegree = getUserDesiredExpansionDegree();

        try{
            engine.expand(expansionDegree);
            System.out.println(String.format("Program expanded successfully to degree %s.", expansionDegree));
        } catch (NumberNotInRangeException e) {
            System.out.println(String.format("Invalid Input: Please enter an Integer number in range (0 - %d)", engine.getMaximalDegree()));
            System.out.println("Please Try Again.");
            expand();
        }
//
//        try {
//            int expandDegree = Integer.parseInt(userInput);
//            engine.expand(expandDegree);
//            System.out.println(String.format("Program expanded successfully to degree %s.", userInput));
//        } catch (NumberFormatException | NumberNotInRangeException e) {
//            System.out.println(String.format("Invalid Input: Please enter an Integer number in range (0 - %d)", maximalDegree));
//            System.out.println("Please Try Again.");
//            expand();
//        }
    }

    private int getUserDesiredExpansionDegree() {
        int expansionDegree = 0;
        int maximalDegree = engine.getMaximalDegree();
        System.out.println(String.format("\nMaximal Degree: %d", maximalDegree));
        if (maximalDegree == 0) {
            System.out.println("All instructions are basic. Nothing to expand.");
        }
        else{
            System.out.println(String.format("Enter expand degree (0 - %d): ", maximalDegree));
            while(!inputScanner.hasNextInt()) {
                System.out.println(String.format("Invalid Input: Please enter an Integer number in range (0 - %d)", maximalDegree));
                System.out.println("Please Try Again.");
                inputScanner.nextLine();
            }

            expansionDegree = Integer.parseInt(inputScanner.nextLine());
        }

        return expansionDegree;
    }

    @Override
    public void runLoadedProgram() {
        int expansionDegree = getUserDesiredExpansionDegree();

        System.out.print("Available program inputs: ");
        System.out.println(String.format("%s", engine.getProgramInputsNames()));
        System.out.println("Enter input values separated by commas (e.g: 5,10,15): ");
        String inputs = inputScanner.nextLine();
        System.out.println("\nRunning program with the following inputs: " + inputs + "\n");
        try {
            showProgramRunResults(engine.runLoadedProgram(expansionDegree, inputs));
        } catch (NumberFormatException e){
            System.out.println("Invalid input. Use integers separated by commas, e.g. 1,2,3.");
        }
    }

    @Override
    public void run() {
        while (true) {
            showMainMenuAndExecuteUserChoice();
            System.out.println("\n*** Press Any Key To Return Main Menu... ***");
            inputScanner.nextLine();
        }
//
//        loadProgram();
//        showProgramDetails();
//        expand();
//        runLoadedProgram();
//        showHistory();
    }

    @Override
    public void showHistory() {
        int i = 1;
        List<ExecutionRecord> history = engine.getHistory();
        System.out.println("**********************");
        System.out.println("Execution History:");
        System.out.println("**********************");

        for (ExecutionRecord executionRecord : history) {
            System.out.println(String.format("\nExecution #%d ", i++));
            System.out.println("----------------------");

            System.out.println(String.format("- Run Degree: %d", executionRecord.getDegree()));
            System.out.println("\n- Input Values:");
            for (Variable variable : executionRecord.getInputVariables().keySet()) {
                System.out.println(String.format("  %s = %d ", variable.getRepresentation(), executionRecord.getInputVariables().get(variable)));
            }

            System.out.println(String.format("\n- y Result: %d", executionRecord.getY()));
            System.out.println(String.format("\n- Total Cycles Count: %d", executionRecord.getTotalCycles()));
        }

        System.out.println("**********************");
    }

    @Override
    public void quitProgram() {

    }

    private void showProgramRunResults(Map<Variable, Long> results) {
        long yValue = results.get(Variable.RESULT);
        Map<Variable, Long> inputVariables = Utils.extractVariablesTypesFromMap(results, VariableType.INPUT);
        Map<Variable, Long> workVariables = Utils.extractVariablesTypesFromMap(results, VariableType.WORK);

        inputVariables = sortVariablesByTheirNumber(inputVariables);
        workVariables = sortVariablesByTheirNumber(workVariables);
        System.out.println("**********************");
        System.out.println("Program Run Results:");
        System.out.println("----------------------");
        System.out.println(String.format("y = %d ", yValue));

        for (Variable variable : inputVariables.keySet()) {
            System.out.println(String.format("%s = %d ", variable.getRepresentation(), inputVariables.get(variable)));
        }

        for (Variable variable : workVariables.keySet()) {
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
