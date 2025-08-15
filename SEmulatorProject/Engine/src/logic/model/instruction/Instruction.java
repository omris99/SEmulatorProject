package logic.model.instruction;

import logic.model.Argument;
import logic.model.execution.ExecutionContext;
import logic.model.label.Label;
import logic.model.variable.Variable;

import java.util.Map;

public interface Instruction {
    String getName();
    Label execute(ExecutionContext context);
    int getCycles();
    Label getLabel();
    Variable getVariable();
    String getInstructionDisplayFormat();
}
