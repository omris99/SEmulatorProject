package model.mappers;

import model.Instruction;
import model.Instructions;
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

        List<Instruction> instructionsList = new ArrayList<>();

        for(SInstruction instruction : jaxbProgram.getSInstructions().getSInstruction())
        {
            instructionsList.add(InstructionMapper.toDomain(instruction));
        }

        Instructions instructions = new Instructions(instructionsList);


        return new Program(jaxbProgram.getName(), instructions);
    }
}
