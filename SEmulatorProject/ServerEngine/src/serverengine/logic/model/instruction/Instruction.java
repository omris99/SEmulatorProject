package serverengine.logic.model.instruction;

import clientserverdto.InstructionDTO;
import serverengine.logic.execution.ExecutionContext;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import types.modeltypes.ArchitectureType;
import types.modeltypes.InstructionType;

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
    void setBreakpoint(boolean isBreakpointSet);
    boolean getBreakpoint();
    ArchitectureType getArchitectureType();
}
