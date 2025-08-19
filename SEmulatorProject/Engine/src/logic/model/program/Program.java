package logic.model.program;

import dto.DTO;
import logic.model.instruction.Instruction;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.util.List;
import java.util.Set;

public interface Program {

    String getName();
    void addInstruction(Instruction instruction);
    List<Instruction> getInstructions();
    Program getExpandedProgram(int degree);
    Set<Label> getAllInstructionsLabels();
    Set<Variable> getAllInstructionsInputs();
    Set<Variable> getAllInstructionsWorkVariables();
    Label validate();
    int calculateMaxDegree();
    int calculateCycles();
    int getMaximalDegree();
    DTO createDTO();
}
