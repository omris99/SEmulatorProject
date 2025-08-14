package logic.model.program;

import logic.model.instruction.Instruction;

import java.util.List;

public interface Program {

    String getName();
    void addInstruction(Instruction instruction);
    List<Instruction> getInstructions();

    boolean validate();
    int calculateMaxDegree();
    int calculateCycles();
}
