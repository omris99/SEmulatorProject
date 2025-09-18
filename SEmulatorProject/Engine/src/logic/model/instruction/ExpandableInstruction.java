package logic.model.instruction;

import logic.model.argument.label.Label;
import logic.model.program.Function;

import java.util.List;
import java.util.Map;

public interface ExpandableInstruction {
    List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel);
}
