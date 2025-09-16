package logic.model.mappers;

import logic.model.argument.NameArgument;
import logic.model.generated.SFunction;
import logic.model.generated.SInstruction;
import logic.model.generated.SProgram;
import logic.model.instruction.Instruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.synthetic.QuoteInstruction;
import logic.model.program.Function;
import logic.model.program.Program;
import logic.model.program.ProgramImpl;

import java.util.ArrayList;
import java.util.List;

public class ProgramMapper {
    public static Program toDomain(SProgram jaxbProgram) {
        if (jaxbProgram == null) {
            return null;
        }

        ProgramImpl domainProgram = new ProgramImpl(jaxbProgram.getName());

        List<String> functionNames = new ArrayList<>();
        for(SFunction jaxbFunction : jaxbProgram.getSFunctions().getSFunction()){
            functionNames.add(jaxbFunction.getName());
        }

        for(SInstruction instruction : jaxbProgram.getSInstructions().getSInstruction())
        {
            domainProgram.addInstruction(InstructionMapper.toDomain(instruction, functionNames));
        }
//
        for (SFunction jaxbFunction : jaxbProgram.getSFunctions().getSFunction()){
            domainProgram.addFunction(FunctionMapper.toDomain(jaxbFunction, functionNames));
        }


        for(Instruction instruction : domainProgram.getInstructions()){
            if(instruction instanceof QuoteInstruction){
                NameArgument functionName = (NameArgument)(((QuoteInstruction) instruction).getArguments().get(InstructionArgument.FUNCTION_NAME));
                for(Function function : domainProgram.getFunctions()){
                    if(functionName.getRepresentation().equals(function.getName())){
                        ((QuoteInstruction) instruction).setContextFunction(function);
                        break;
                    }
                }
            }
        }

        return domainProgram;
    }
}
