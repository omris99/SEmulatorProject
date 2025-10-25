package serverengine.logic.model.instruction;

import serverengine.logic.model.argument.Argument;

import java.util.Map;

public interface InstructionWithArguments {
    Map<InstructionArgument, Argument> getArguments();
}
