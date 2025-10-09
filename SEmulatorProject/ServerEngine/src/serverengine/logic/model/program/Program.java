package serverengine.logic.model.program;

import clientserverdto.DTO;
import serverengine.logic.instructiontree.InstructionsTree;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.instruction.Instruction;
import types.ArchitectureType;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Program extends Serializable {

    String getName();
    String getRepresentation();
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
    ArchitectureType getMinimalArchitectureType();
}
