package http;

public class ServerPaths {
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/SEmulatorServer_Web_exploded";
    private final static String EXECUTION_CONTEXT_PATH = "/execution";
    private final static String DEBUG_CONTEXT_PATH = EXECUTION_CONTEXT_PATH + "/debug";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOAD_FILE = FULL_SERVER_PATH + "/loadFile";
    public final static String GET_EXPANDED_PROGRAM = FULL_SERVER_PATH + "/expandedProgram";
    public final static String CHANGE_ON_SCREEN_PROGRAM = FULL_SERVER_PATH + "/changeOnScreenProgram";
    public final static String GET_HISTORY = FULL_SERVER_PATH + "/history";
    public final static String GET_LOADED_PROGRAM = FULL_SERVER_PATH + "/loadedProgram";
    public final static String GET_INPUTS_NAMES = FULL_SERVER_PATH + "/programInputsNames";

    //EXECUTION
    public final static String RUN_PROGRAM = FULL_SERVER_PATH + EXECUTION_CONTEXT_PATH + "/runProgram";

    //DEBUGGING
    public final static String GET_NEXT_INSTRUCTION_TO_EXECUTE = FULL_SERVER_PATH + DEBUG_CONTEXT_PATH + "/nextInstructionToExecute";
    public final static String STEP_OVER = FULL_SERVER_PATH + DEBUG_CONTEXT_PATH + "/stepOver";
    public final static String STEP_BACKWARD = FULL_SERVER_PATH + DEBUG_CONTEXT_PATH + "/stepBackward";
    public final static String INIT_DEBUGGING_SESSION = FULL_SERVER_PATH + DEBUG_CONTEXT_PATH + "/initDebuggingSession";
    public final static String RESUME_DEBUGGER_EXECUTION = FULL_SERVER_PATH + DEBUG_CONTEXT_PATH + "/resume";
    public final static String STOP_DEBUGGER_EXECUTION = FULL_SERVER_PATH + DEBUG_CONTEXT_PATH + "/stop";
    public final static String UPDATE_INSTRUCTION_BREAKPOINT = FULL_SERVER_PATH + DEBUG_CONTEXT_PATH + "/updateInstructionBreakPoint";


}
