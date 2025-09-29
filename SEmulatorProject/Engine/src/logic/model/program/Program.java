package logic.model.program;

import dto.DTO;
import logic.instructiontree.InstructionsTree;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.Instruction;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Program extends Serializable {

    String getName();
    void addInstruction(Instruction instruction);
    List<Instruction> getInstructions();
    Program getExpandedProgram(int degree);
    Set<Label> getAllInstructionsLabels();
    Set<Variable> getAllInstructionsInputs();
    Set<Variable> getAllInstructionsWorkVariables();
    Label validate();
    int getMaximalDegree();
    DTO createDTO();
    int getDegree();
    InstructionsTree getInstructionsTree();
}
