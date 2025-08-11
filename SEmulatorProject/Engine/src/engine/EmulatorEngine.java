package engine;


import model.Instruction;
import model.Program;

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
    public void loadProgram() {
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
