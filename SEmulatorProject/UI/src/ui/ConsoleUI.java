package ui;

import engine.EmulatorEngine;
import model.*;
/*
 * TODO:
 *  1. IMPLEMENT loadProgram
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
        String xmlPath = "/Users/omrishtruzer/Downloads/badic.xml";
        engine.loadProgram(xmlPath);
    }

    @Override
    public void showProgramDetails() {
        int i = 0;

        System.out.println(String.format("Program Name: %s", engine.getProgramName()));
        System.out.println(String.format("Inputs Names: %s", engine.getProgramInputsNames()));
        System.out.println(String.format("Labels Names: %s", engine.getProgramLabelsNames()));
        for(Instruction instruction : engine.getInstructions())
        {
            System.out.print(String.format("#%d ", i));
            System.out.print(instruction);
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
