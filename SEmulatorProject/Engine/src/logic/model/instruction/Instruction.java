package logic.model.instruction;

import logic.execution.ExecutionContext;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

public interface Instruction extends Cloneable{
    String getName();
    Label execute(ExecutionContext context);
    int getCycles();
    Label getLabel();
    Variable getVariable();
    String getInstructionDisplayFormat();
    String getType();
    int getDegree();
    int computeDegree();
    void setIndex(int index);
    int getIndex();
    void setParent(Instruction parent);
    Instruction clone();
}
