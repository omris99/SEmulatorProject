package logic.model.instruction;

import logic.exceptions.UnknownLabelReferenceExeption;
import logic.model.variable.VariableOld;

import java.util.*;

public class Instructions {
    private final List<InstructionOld> instructions;
    private Set<String> instructionsLabels;
    private Set<VariableOld> instructionsInputs;

    public Instructions(List<InstructionOld> instructions) {
        this.instructions = instructions;
        this.instructionsLabels = new LinkedHashSet<>();
        this.instructionsInputs = new LinkedHashSet<>();
        boolean isExitLabelReferenceExist = false;

        for(InstructionOld instruction : instructions){
            VariableOld variable = instruction.getVariable();
            Map<InstructionArgument, String> arguments = instruction.getArguments();
            if(variable.isInputVariable()){
                this.instructionsInputs.add(variable);
            }

            if(arguments != null){
                for(InstructionArgument argument : arguments.keySet())
                {
                    if(argument.getType().equals("variable")){
                        VariableOld argumentVariable = VariableOld.parse(arguments.get(argument));
                        if(argumentVariable.isInputVariable()){
                            this.instructionsInputs.add(argumentVariable);
                        }
                    }

                    if(argument.getType().equals("label") && arguments.get(argument).equals("EXIT")) {
                        isExitLabelReferenceExist = true;
//                        this.instructionsLabels.add(arguments.get(argument));
                    }
                }
            }

            if(instruction.isLabled()) {
                this.instructionsLabels.add(instruction.getLabel());
            }
        }

        if(isExitLabelReferenceExist) {
            this.instructionsLabels.add("EXIT");
        }
        throwExceptionIfLabelsArgumentsInvalid();
    }

    public void add(InstructionOld instruction) {
        this.instructions.add(instruction);
        VariableOld variable = instruction.getVariable();

        if(variable.isInputVariable()){
            this.instructionsInputs.add(variable);
        }

        if(instruction.isLabled()) {
            this.instructionsLabels.add(instruction.getLabel());
        }
    }

    public Set<String> getLabels() {
        return instructionsLabels;
    }

    public Set<VariableOld> getInputs() {
        return instructionsInputs;
    }

    public List<InstructionOld> getInstructionsList() {
        return instructions;
    }

    private void throwExceptionIfLabelsArgumentsInvalid() {
        for(InstructionOld instruction : instructions) {
            Map<InstructionArgument, String> arguments = instruction.getArguments();
            if(arguments != null){
                for(InstructionArgument instructionArgument : arguments.keySet()){
                    if(instructionArgument.getType().equals("label")) {
                        String label = arguments.get(instructionArgument);
                        if(!instructionsLabels.contains(label) && !label.equals("EXIT")){
                            throw new UnknownLabelReferenceExeption(label);
                        }
                    }
                }
            }
        }
    }
}
