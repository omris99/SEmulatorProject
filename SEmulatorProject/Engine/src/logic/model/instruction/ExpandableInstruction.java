package logic.model.instruction;

import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.util.List;
import java.util.Set;

public interface ExpandableInstruction {
    List<Instruction> expand(Set<Label> programLabels, Set<Variable> programWorkVariables, Set<Variable> programInputVariables, Label instructionLabel);
}
