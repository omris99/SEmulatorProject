package logic.exceptions;

import logic.model.argument.ArgumentType;

public enum ArgumentErrorType {
    LABEL_MUST_START_WITH_L("Label not starts with L", ArgumentType.LABEL),
    VARIABLE_PREFIX_INVALID("Variable name must start with X, Y, or Z", ArgumentType.VARIABLE),
    Y_VARIABLE_HAS_INDEX("Y Variable cannot have an index", ArgumentType.VARIABLE),
    VARIABLE_INDEX_MISSING("Variable index is missing", ArgumentType.VARIABLE),
    VARIABLE_INDEX_CANT_PARSE_TO_NUMBER("Variable index must be an integer number", ArgumentType.VARIABLE),
    VARIABLE_INDEX_IS_NEGATIVE("Variable index cannot be negative", ArgumentType.VARIABLE),
    CONSTANT_MUST_BE_A_NUMBER("Constant must be a numeric value", ArgumentType.CONSTANT);

    private final String userMessage;
    private final ArgumentType argumentType;

    ArgumentErrorType(String userMessage, ArgumentType type) {
        this.userMessage = userMessage;
        this.argumentType = type;
    }

    public String getUserMessage(){
        return userMessage;
    }
    public ArgumentType getArgumentType(){
        return argumentType;
    }
}
