package gui.app;

import dto.ProgramDTO;
import gui.components.header.headerController;
import gui.components.instructionswindow.InstructionWindowController;
import gui.components.loadfilebar.LoadFileBarController;
import javafx.fxml.FXML;
import logic.engine.EmulatorEngine;
import logic.engine.Engine;

public class AppController {
    private final Engine emulatorEngine;
    @FXML
    private headerController headerController;

    @FXML
    private LoadFileBarController loadFileBarController;

    @FXML
    private InstructionWindowController instructionWindowController;

    @FXML
    private void initialize() {
//        headerController.setAppController(this);
        loadFileBarController.setAppController(this);

    }

    public AppController() {
        this.emulatorEngine = new EmulatorEngine();
    }

    public void loadProgram(String filePath) throws Exception{
        emulatorEngine.loadProgram(filePath);
        System.out.println("Program loaded");
        instructionWindowController.setInstructionsTableData(((ProgramDTO) emulatorEngine.getLoadedProgramDTO()).getInstructionsDTO());
    }


}
