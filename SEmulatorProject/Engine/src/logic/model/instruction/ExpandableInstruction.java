package logic.model.instruction;

import logic.model.argument.label.Label;
import java.util.List;

public interface ExpandableInstruction {
    List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel);
}
