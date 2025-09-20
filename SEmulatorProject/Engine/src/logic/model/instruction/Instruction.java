package logic.model.instruction;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.io.Serializable;
import java.util.List;

public interface Instruction extends Cloneable, Serializable {
    String getName();
    Label execute(ExecutionContext context);
    int getCycles();
    Label getLabel();
    Variable getVariable();
    String getInstructionDisplayFormat();
    InstructionDTO getInstructionDTO();
    InstructionType getType();
    int getDegree();
    void setIndex(int index);
    int getIndex();
    void setParent(Instruction parent);
    Instruction clone();
    List<String> getAssociatedArgumentsAndLabels();
    Instruction getParent();
}
