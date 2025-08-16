package logic.model.program;

import logic.exceptions.UnknownLabelReferenceExeption;
import logic.model.instruction.Instruction;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.util.List;
import java.util.Set;

public interface Program {

    String getName();
    void addInstruction(Instruction instruction);
    List<Instruction> getInstructions();
    Set<Label> getAllInstructionsLabels();
    Set<Variable> getAllInstructionsInputs();
    void validate() throws UnknownLabelReferenceExeption;
    int calculateMaxDegree();
    int calculateCycles();
}
