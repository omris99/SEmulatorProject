package ui;

import dto.ProgramDTO;
import logic.engine.EmulatorEngine;
import logic.exceptions.InvalidXmlFileException;
import jakarta.xml.bind.JAXBException;
import logic.execution.ExecutionRecord;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableType;
import logic.utils.Utils;
import logic.utils.serialization.SerializationManager;
import ui.menu.MainMenu;
import ui.menu.Menu;
import ui.menu.option.MainMenuOption;
import ui.menu.option.MenuOption;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/*
 * TODO:
 *  1. search for xml application errors and make sure the errors exceptions corrrectly
 *  2. ensure that every exception message is fully detailed.
 *  3. improve the implemetntation of VariableImpl class (parse)
 */

public class ConsoleUI implements UI {
    private EmulatorEngine engine;
    private final Scanner inputScanner;
    private final Menu mainMenu;

    public ConsoleUI() {
        engine = new EmulatorEngine();
        inputScanner = new Scanner(System.in);
        mainMenu = buildMainMenu();
    }

    public static void main(String[] args) {
        UI ui = new ConsoleUI();
        ui.run();
    }

    private Menu buildMainMenu() {
        List<MenuOption> options = new ArrayList<>();
        options.addAll(Arrays.asList(MainMenuOption.values()));
        return new MainMenu(options, "S-Emulator");
    }

    @Override
    public void showMainMenuAndExecuteUserChoice() {
        MenuOption choice = mainMenu.printAndReturnUserChoice();
        executeMainMenuOption(choice);
    }

    private void executeMainMenuOption(MenuOption option) {
        boolean isCanExecuteBeforeProgramLoading = (engine.isProgramLoaded() || option.equals(MainMenuOption.LOAD_PROGRAM)
                || option.equals(MainMenuOption.EXIT) || option.equals(MainMenuOption.LOAD_SYSTEM) || option.equals(MainMenuOption.SAVE_SYSTEM));

        if (!isCanExecuteBeforeProgramLoading) {
            printError("Error: Cant invoke '" + option.getMenuDisplay() + "' Because Program Not Loaded yet.");
        } else {
            switch (option) {
                case MainMenuOption.LOAD_PROGRAM -> loadProgram();
                case MainMenuOption.SHOW_PROGRAM -> showProgramDetails((ProgramDTO) engine.getLoadedProgramDTO());
                case MainMenuOption.EXPAND_PROGRAM -> expand();
                case MainMenuOption.RUN_PROGRAM -> runLoadedProgram();
                case MainMenuOption.SHOW_HISTORY -> showHistory();
                case MainMenuOption.SAVE_SYSTEM -> saveSystemState();
                case MainMenuOption.LOAD_SYSTEM -> loadSystemState();
                case MainMenuOption.EXIT -> quitProgram();
                default -> throw new IllegalStateException("Unexpected value: " + option);
            }
        }
    }

    @Override
    public void loadProgram() {
        System.out.print("Please enter program full path: ");
        String xmlPath = inputScanner.nextLine();
//        String xmlPath = "/Users/omrishtruzer/Documents/SEmulatorProject/Test XMLFiles/custom-1.xml";
        try {
            engine.loadProgram(xmlPath);
            System.out.println("XML FILE: " + xmlPath + " Loaded successfully.");
        } catch (InvalidXmlFileException e) {
            switch (e.getType()) {
                case FILE_MISSING:
                    printError("File not found: " + e.getFilePath());
                    break;
                case INVALID_EXTENSION:
                    printError("Invalid file type: " + e.getFilePath() + " must be .xml");
                    break;
                case UNKNOWN_LABEL:
                    printError("Unknown label in file " + e.getFilePath() + ": " + e.getElement());
                    break;
                case INVALID_ELEMENT:
                    printError("Unrecognized Label in file " + e.getFilePath() + ". " + "\nLabel: " + e.getElement());
                    break;
            }
        } catch (JAXBException e) {
            printError("Can't read XML File: " + e.getMessage());
        }
    }

    private void printError(String message) {
        System.out.println("\n------ ERROR! ------");
        System.out.println(message);
        System.out.println();
    }


    @Override
    public void showProgramDetails(ProgramDTO programDetails) {
        System.out.println(String.format("Program Name: %s", programDetails.getName()));
        System.out.println(String.format("Inputs Names: %s", programDetails.getInputNames()));
        System.out.println(String.format("Labels Names: %s", programDetails.getLabelsNames()));
        for (String instruction : programDetails.getInstructionsInDisplayFormat()) {
            System.out.println(instruction);
        }
    }

    @Override
    public void expand() {
        int expansionDegree = getUserDesiredExpansionDegree();

        showProgramDetails((ProgramDTO) engine.getExpandedProgramDTO(expansionDegree));
        System.out.println(String.format("Program expanded successfully to degree %s.", expansionDegree));

    }

    private int getUserDesiredExpansionDegree() {
        int maximalDegree = engine.getMaximalDegree();
        int expansionDegree = 0;

        System.out.println(String.format("Maximal Degree: %d", maximalDegree));
        if (maximalDegree == 0) {
            System.out.println("All instructions are basic. Nothing to expand.");
        }
        else {
            while (true) {
                System.out.print(String.format("Enter expand degree (0 - %d): ", maximalDegree));
                if (!inputScanner.hasNextInt()) {
                    printError(String.format("Invalid input: Please enter an integer number.", maximalDegree));
                    inputScanner.nextLine();
                    continue;
                }

                expansionDegree = inputScanner.nextInt();
                inputScanner.nextLine();

                if (expansionDegree >= 0 && expansionDegree <= maximalDegree) {
                    break;
                } else {
                    printError(String.format("Invalid input: Number out of range. Please enter an integer number in range (0 - %d).", maximalDegree));
                }
            }
        }

        return expansionDegree;
    }

    @Override
    public void runLoadedProgram() {
        int expansionDegree = getUserDesiredExpansionDegree();
        ProgramDTO loadedProgramDTO = (ProgramDTO) engine.getLoadedProgramDTO();

        System.out.print("Available program inputs: ");
        System.out.println(String.format("%s", loadedProgramDTO.getInputNames()));
        System.out.println("Enter input values separated by commas (e.g: 5,10,15): ");
        String inputs = inputScanner.nextLine();
        System.out.println("\nRunning program with the following inputs: " + inputs + "\n");
        try {
            showProgramRunResults(engine.runLoadedProgram(expansionDegree, inputs));
        } catch (NumberFormatException e) {
            printError("Invalid input. Use integers separated by commas, e.g. 1,2,3.");
        }
    }

    @Override
    public void run() {
        while (true) {
            showMainMenuAndExecuteUserChoice();
            System.out.println("\n*** Press 'Enter' Key To Return Main Menu... ***");
            inputScanner.nextLine();
        }
    }

    @Override
    public void showHistory() {
        int i = 1;
        List<ExecutionRecord> history = engine.getHistory();
        System.out.println("\n***********************************************");
        System.out.println("               Execution History:");
        System.out.println("***********************************************");

        for (ExecutionRecord executionRecord : history) {
            System.out.println("\n----------------------");
            System.out.println(String.format("Execution #%d ", i++));
            System.out.println("----------------------");

            System.out.println(String.format("- Run Degree: %d", executionRecord.getDegree()));
            System.out.println("\n- Input Values:");
            for (Variable variable : executionRecord.getInputVariables().keySet()) {
                System.out.println(String.format("  %s = %d ", variable.getRepresentation(), executionRecord.getInputVariables().get(variable)));
            }

            System.out.println(String.format("\n- y Result: %d", executionRecord.getY()));
            System.out.println(String.format("\n- Total Cycles Count: %d", executionRecord.getTotalCycles()));
        }

        System.out.println("\n******************** E N D ********************");
        System.out.println("***********************************************");
    }

    @Override
    public void quitProgram() {
        System.out.println("GOOD BYE !");
        engine.quit();
    }

    private void showProgramRunResults(Map<Variable, Long> results) {
        long yValue = results.get(Variable.RESULT);
        Map<Variable, Long> inputVariables = Utils.extractVariablesTypesFromMap(results, VariableType.INPUT);
        Map<Variable, Long> workVariables = Utils.extractVariablesTypesFromMap(results, VariableType.WORK);

        inputVariables = sortVariablesByTheirNumber(inputVariables);
        workVariables = sortVariablesByTheirNumber(workVariables);
        System.out.println("\n***********************************************");
        System.out.println("              Program Run Results:");
        System.out.println("***********************************************\n");
        System.out.println(String.format("y = %d ", yValue));
        for (Variable variable : inputVariables.keySet()) {
            System.out.println(String.format("%s = %d ", variable.getRepresentation(), inputVariables.get(variable)));
        }

        for (Variable variable : workVariables.keySet()) {
            System.out.println(String.format("%s = %d ", variable.getRepresentation(), workVariables.get(variable)));
        }

        System.out.println(String.format("Cycles Count = %d ", engine.getLastExecutionCycles()));
        System.out.println("\n******************** E N D ********************");
        System.out.println("***********************************************");
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

    private void saveSystemState() {
        System.out.print("Enter full path (without file extension): ");
        String path = inputScanner.nextLine();

        try {
            SerializationManager.save(engine, path);
            System.out.println("System State successfully saved in :" + path + ".ser");
        } catch (IOException e) {
            printError("Save state try failed:" + e.getMessage());
        }
    }

    private void loadSystemState() {
        System.out.print("Enter full path (without file extension): ");
        String path = inputScanner.nextLine();

        try {
            EmulatorEngine loadedEngine = SerializationManager.load(path, EmulatorEngine.class);
            this.engine = loadedEngine;
            System.out.println("System State successfully saved from :" + path + ".ser");
        } catch (IOException | ClassNotFoundException e) {
            printError("Load state try failed:" + e.getMessage());
        }
    }
}
