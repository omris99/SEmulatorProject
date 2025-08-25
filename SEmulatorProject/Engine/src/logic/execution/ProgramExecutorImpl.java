package logic.execution;

import logic.model.instruction.Instruction;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.program.Program;
import logic.model.argument.variable.Variable;


import java.util.*;

public class ProgramExecutorImpl implements ProgramExecutor{

    private final Program program;
    private InstructionsQueue instructionsQueue;
    private int cyclesCount;

    public ProgramExecutorImpl(Program program) {
        this.program = program;
        instructionsQueue = new InstructionsQueue(program.getInstructions());
        cyclesCount = 0;
    }

    @Override
    public Map<Variable, Long> run(Map<Variable, Long> inputVariablesMap) {
        ExecutionContext context = new ExecutionContextImpl(inputVariablesMap,
                program.getAllInstructionsWorkVariables());
        instructionsQueue = new InstructionsQueue(program.getInstructions());
        Instruction currentInstruction = instructionsQueue.getFirstInQueue();
        Label nextLabel;

        do {
            nextLabel = currentInstruction.execute(context);
            cyclesCount += currentInstruction.getCycles();

            if (nextLabel == FixedLabel.EMPTY) {
                currentInstruction = instructionsQueue.next();
            } else if (nextLabel != FixedLabel.EXIT) {
                instructionsQueue.setQueueBegin(nextLabel);
                currentInstruction = instructionsQueue.getFirstInQueue();
            }
        } while (nextLabel != FixedLabel.EXIT && currentInstruction != null);

        return context.getVariablesStatus();
    }

    public int getCyclesCount(){
        return cyclesCount;
    }

    @Override
    public Map<Variable, Long> variableState() {
        return Map.of();
    }
}
