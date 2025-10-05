package logic.execution;

import logic.model.argument.label.Label;
import logic.model.instruction.Instruction;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class InstructionsQueue implements Serializable {
    private final List<Instruction> allInstructions;
    private final List<Instruction> queue;
    private int currentInstructionIndex;
    List<Integer> executedInstructionsIndexes;



    public InstructionsQueue(List<Instruction> allInstructions) {
        this.allInstructions = allInstructions;
        this.queue = allInstructions;
        this.executedInstructionsIndexes = new LinkedList<>();
    }

    public Instruction next() {
        executedInstructionsIndexes.add(currentInstructionIndex);
        currentInstructionIndex++;

        return currentInstructionIndex < queue.size() ? queue.get(currentInstructionIndex) : null;
    }

    public Instruction prev(){
        if (executedInstructionsIndexes.isEmpty()) {
            currentInstructionIndex = 0;
        }
        else{
            currentInstructionIndex = executedInstructionsIndexes.removeLast();
        }

        return queue.get(currentInstructionIndex);
    }

    public Instruction jumpToLabel(Label labelToBeginFrom)
    {
        executedInstructionsIndexes.add(currentInstructionIndex);
        int instructionLabelToBeginFromIndex = 0;
        Instruction instructionToBeginFrom = null;
        for(Instruction instruction : allInstructions){
            if(instruction.getLabel().equals(labelToBeginFrom)){
                currentInstructionIndex = instructionLabelToBeginFromIndex;
                instructionToBeginFrom = instruction;
                break;
            }
            instructionLabelToBeginFromIndex++;
        }

        return instructionToBeginFrom;
    }

    public Instruction getFirstInQueue()
    {
        return queue.getFirst();
    }
}
