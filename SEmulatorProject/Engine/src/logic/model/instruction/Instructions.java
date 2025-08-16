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

    public Instructions() {
        this.instructions = new ArrayList<>();
        this.instructionsLabels = new LinkedHashSet<>();
        this.instructionsInputs = new LinkedHashSet<>();

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

    public void add(Instruction instruction) {
        this.instructions.add(instruction);
        Variable variable = instruction.getVariable();

        if (variable.getType() == VariableType.INPUT) {
            this.instructionsInputs.add(variable);
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

    public Set<Label> getLabels() {
        return instructionsLabels;
    }

    public Set<Variable> getInputs() {
        return instructionsInputs;
    }

    public List<Instruction> getInstructionsList() {
        return instructions;
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
