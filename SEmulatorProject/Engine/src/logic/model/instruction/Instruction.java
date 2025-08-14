package logic.model.instruction;

import logic.model.execution.ExecutionContext;
import logic.model.label.Label;
import logic.model.variable.Variable;

public interface Instruction {
    String getName();
    Label execute(ExecutionContext context);
    int getCycles();
    Label getLabel();
    Variable getVariable();
}
