package gui.app;

import dto.ProgramDTO;
import gui.components.instructionswindow.InstructionWindowController;
import gui.components.loadfilebar.LoadFileBarController;
import javafx.fxml.FXML;
import logic.engine.EmulatorEngine;
import logic.engine.Engine;

public class AppController {
    private final Engine emulatorEngine;

    @FXML
    private LoadFileBarController loadFileBarController;

    @FXML
    private InstructionWindowController instructionWindowController;

    @FXML
    private void initialize() {
        loadFileBarController.setAppController(this);
    }

    public AppController() {
        this.emulatorEngine = new EmulatorEngine();
    }

    public void loadProgram(String filePath) throws Exception{
        emulatorEngine.loadProgram(filePath);
        instructionWindowController.setInstructionsTableData(((ProgramDTO) emulatorEngine.getLoadedProgramDTO()).getInstructionsDTO());
    }


}
