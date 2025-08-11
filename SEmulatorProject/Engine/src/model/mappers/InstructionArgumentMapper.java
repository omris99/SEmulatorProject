package model.mappers;

import model.InstructionArgument;
import model.generated.SInstructionArgument;

public class InstructionArgumentMapper {
    public static InstructionArgument toDomain(SInstructionArgument jaxbArgument) {
        if (jaxbArgument == null) {
            return null;
        }

        InstructionArgument domainArgument = new InstructionArgument(jaxbArgument.getName(), jaxbArgument.getValue());

        return domainArgument;
    }
}
