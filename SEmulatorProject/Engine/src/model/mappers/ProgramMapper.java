package model.mappers;

import model.Instruction;
import model.Program;
import model.generated.SInstruction;
import model.generated.SProgram;

import java.util.ArrayList;
import java.util.List;

public class ProgramMapper {
    public static Program toDomain(SProgram jaxbProgram) {
        if (jaxbProgram == null) {
            return null;
        }

        List<Instruction> instructions = new ArrayList<>();

        for(SInstruction instruction : jaxbProgram.getSInstructions().getSInstruction())
        {
            instructions.add(InstructionMapper.toDomain(instruction));
        }

        return new Program(jaxbProgram.getName(), instructions);
    }
}
