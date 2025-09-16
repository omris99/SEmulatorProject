package logic.model.instruction;

import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.program.Program;
import logic.model.program.ProgramImpl;

import java.util.List;
import java.util.Set;

public interface ExpandableInstruction {
    List<Instruction> expand(Program program, Label instructionLabel);
}
