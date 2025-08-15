package logic.model.program;

import logic.model.instruction.Instruction;
import logic.model.label.Label;
import logic.model.variable.Variable;

import java.util.List;
import java.util.Set;

public interface Program {

    String getName();
    void addInstruction(Instruction instruction);
    List<Instruction> getInstructions();
    Set<Label> getAllInstructionsLabels();
    Set<String> getAllInputsNames();
    boolean validate();
    int calculateMaxDegree();
    int calculateCycles();
}
