package gui.app;

import dto.ProgramDTO;
import gui.components.instructionswindow.InstructionWindowController;
import gui.components.loadfilebar.LoadFileBarController;
import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import logic.engine.EmulatorEngine;
import logic.engine.Engine;
import logic.exceptions.InvalidArgumentException;
import logic.exceptions.InvalidXmlFileException;

import java.io.File;

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

    public void loadProgramWithProgress(File selectedFile) {
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try{
                    updateMessage("Loading program...");
                    emulatorEngine.loadProgram(selectedFile.getAbsolutePath());

                    updateProgress(50, 100);
                    Thread.sleep(100);

                    ProgramDTO programDTO = (ProgramDTO) emulatorEngine.getLoadedProgramDTO();
                    instructionWindowController.setInstructionsTableData(programDTO.getInstructionsDTO());

                    updateProgress(100, 100);
                    updateMessage(selectedFile.getAbsolutePath());
                }


                catch (Exception e){
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Loading Program");
                        alert.setHeaderText("Failed to load XML file");

                        String content;
                        switch (e) {
                            case InvalidXmlFileException iv -> {
                                switch (iv.getType()) {
                                    case FILE_MISSING -> content = "File not found: " + iv.getFilePath();
                                    case INVALID_EXTENSION ->
                                            content = "Invalid file type: " + iv.getFilePath() + " must be .xml";
                                    case UNKNOWN_LABEL ->
                                            content = String.format("No instruction labeled as %s exists in the program.", iv.getElement());
                                    default -> content = "Unknown InvalidXmlFileException";
                                }
                            }
                            case JAXBException jaxb -> content = "Can't read XML File";
                            case InvalidArgumentException ia ->
                                    content = String.format("%s.  \nError %s: %s ",
                                            ia.getErrorType().getUserMessage(),
                                            ia.getErrorType().getArgumentType(),
                                            ia.getArgumentName());
                            case IllegalArgumentException iae -> content = "Invalid XML File: " + iae.getMessage();
                            default -> content = "Unexpected error: " + e.getMessage();
                        }

                        alert.setContentText(content);
                        alert.showAndWait();
                    });

                    updateMessage("Failed to load file.");
                    updateProgress(0,100);
                }

                return null;
            }
        };

        loadFileBarController.bindTaskToUI(loadTask);

        new Thread(loadTask).start();
    }

}
