package logic.execution;

import logic.model.argument.label.Label;
import logic.model.instruction.Instruction;

import java.io.Serializable;
import java.util.List;

public class InstructionsQueue implements Serializable {
    private final List<Instruction> allInstructions;
    private List<Instruction> queue;
    private int currentInstructionIndex;

    public InstructionsQueue(List<Instruction> allInstructions) {
        this.allInstructions = allInstructions;
        queue = allInstructions;
    }

    public Instruction next()
    {
        currentInstructionIndex++;

        return currentInstructionIndex < queue.size() ? queue.get(currentInstructionIndex) : null;
    }

    public void setQueueBegin(Label labelToBeginFrom)
    {
        int instructionLabelToBeginFromIndex = 0;
        for(Instruction instruction : allInstructions){
            if(instruction.getLabel().equals(labelToBeginFrom)){
                queue = allInstructions.subList(instructionLabelToBeginFromIndex, allInstructions.size());
                currentInstructionIndex = 0;
                break;
            }
            instructionLabelToBeginFromIndex++;
        }
    }

    public Instruction getFirstInQueue()
    {
        return queue.getFirst();
    }
}
