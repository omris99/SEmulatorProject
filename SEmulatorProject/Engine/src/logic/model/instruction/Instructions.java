package logic.model.instruction;

import logic.model.argument.Argument;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableType;

import java.util.*;

public class Instructions {
    private final List<Instruction> instructions;
    private Set<Label> instructionsLabels;
    private Set<Variable> instructionsInputs;
    private Set<Variable> instructionsWorkVariables;
    private int expandLevel;

    public Instructions() {
        this.instructions = new LinkedList<>();
        this.instructionsLabels = new LinkedHashSet<>();
        this.instructionsInputs = new LinkedHashSet<>();
        this.instructionsWorkVariables = new LinkedHashSet<>();

//        boolean isExitLabelReferenceExist = false;
//        for(Instruction instruction : instructions){
//            Variable variable = instruction.getVariable();
//            Map<InstructionArgument, String> arguments = instruction.getArguments();
//            if(variable.isInputVariable()){
//                this.instructionsInputs.add(variable);
//            }
//
//            if(arguments != null){
//                for(InstructionArgument argument : arguments.keySet())
//                {
//                    if(argument.getType().equals("variable")){
//                        Variable argumentVariable = Variable.parse(arguments.get(argument));
//                        if(argumentVariable.isInputVariable()){
//                            this.instructionsInputs.add(argumentVariable);
//                        }
//                    }
//
//                    if(argument.getType().equals("label") && arguments.get(argument).equals("EXIT")) {
//                        isExitLabelReferenceExist = true;
////                        this.instructionsLabels.add(arguments.get(argument));
//                    }
//                }
//            }
//
//            if(instruction.isLabled()) {
//                this.instructionsLabels.add(instruction.getLabel());
//            }
//        }
//
//        if(isExitLabelReferenceExist) {
//            this.instructionsLabels.add("EXIT");
//        }
//        throwExceptionIfLabelsArgumentsInvalid();
    }
    public void add(Instruction instruction, int indexToAdd) {
        this.instructions.add(indexToAdd, instruction);
        instruction.setIndex(indexToAdd + 1);

        Variable variable = instruction.getVariable();

        if (variable.getType() == VariableType.INPUT) {
            this.instructionsInputs.add(variable);
        }
        else if (variable.getType() == VariableType.WORK) {
            this.instructionsWorkVariables.add(variable);
        }

        if (instruction.getLabel() != FixedLabel.EMPTY) {
            this.instructionsLabels.add(instruction.getLabel());
        }

        if (instruction instanceof InstructionWithArguments) {
            Collection<Argument> arguments = ((InstructionWithArguments) instruction).getArguments().values();
            for (Argument argument : arguments) {
                if (argument instanceof Variable) {
                    if (((Variable) argument).getType() == VariableType.INPUT) {
                        this.instructionsInputs.add((Variable) argument);
                    }
                }
                else if (argument instanceof Label) {
                    if(argument == FixedLabel.EXIT) {
                        this.instructionsLabels.add((Label) argument);
                    }
                }
            }
        }
    }

    public void add(Instruction instruction) {
        add(instruction, instructions.size());
    }

    public void addListOfInstructions(List<Instruction> instructions, int index) {
        this.instructions.remove(index);
        int indexToAdd = index;
        for (Instruction instruction : instructions) {
            add(instruction, indexToAdd);
            indexToAdd++;
        }
    }

    public Set<Label> getLabels() {
        return instructionsLabels;
    }

    public Set<Variable> getInputs() {
        return instructionsInputs;
    }

    public Set<Variable> getWorkVariables() {
        return instructionsWorkVariables;
    }

    public List<Instruction> getInstructionsList() {
        return instructions;
    }

    public int getMaximalDegree(){
        int maximalDegree = instructions.stream().map(Instruction::getDegree).max(Comparator.naturalOrder()).get();

        return maximalDegree;
    }

    public void expand() {

        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction instanceof ExpandableInstruction) {
                List<Instruction> expanded = ((ExpandableInstruction) instruction)
                        .expand(getMaxLabelIndex(), getMaxWorkVariableIndex(), instruction.getLabel());
                expanded.stream().forEach(newInstruction -> newInstruction.setParent(instruction));

                addListOfInstructions(expanded, i);
                i += expanded.size() - 1; // skip over newly added
            }
        }

        resetIndexes();
        expandLevel++;
    }


    private int getMaxLabelIndex() {
        return instructionsLabels.stream().map(Argument::getIndex).max(Comparator.naturalOrder()).get();
    }

    private int getMaxWorkVariableIndex() {
        return instructionsWorkVariables.stream().map(Argument::getIndex).max(Comparator.naturalOrder()).get();
    }

    public int getExpandLevel() {
        return expandLevel;
    }

    public void resetIndexes() {
        int index = 1;

        for (Instruction instruction : instructions) {
            instruction.setIndex(index);
            index++;
        }
    }
}

//    private void throwExceptionIfLabelsArgumentsInvalid() {
//        for(Instruction instruction : instructions) {
//            if(instruction instanceof InstructionWithArguments) {
//                Map<InstructionArgument, Argument> arguments =((InstructionWithArguments) instruction).getArguments();
//                for(InstructionArgument instructionArgument : arguments.keySet()){
//                    if(instructionArgument.getType().equals("label")) {
//                        String label = arguments.get(instructionArgument);
//                        if(!instructionsLabels.contains(label) && !label.equals("EXIT")){
//                            throw new UnknownLabelReferenceExeption(label);
//                        }
//                }
//            }
//        }
//    }
//}
