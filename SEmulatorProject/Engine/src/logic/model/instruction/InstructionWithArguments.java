package logic.model.instruction;

import logic.model.argument.Argument;

import java.util.Map;

public interface InstructionWithArguments {
    Map<InstructionArgument, Argument> getArguments();
}
