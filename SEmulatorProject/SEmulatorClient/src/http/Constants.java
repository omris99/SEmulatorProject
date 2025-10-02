package http;

public class Constants {
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/SEmulatorServer_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOAD_FILE = FULL_SERVER_PATH + "/loadFile";
    public final static String GET_EXPANDED_PROGRAM = FULL_SERVER_PATH + "/expandedProgram";
    public final static String CHANGE_ON_SCREEN_PROGRAM = FULL_SERVER_PATH + "/changeOnScreenProgram";
    public final static String GET_HISTORY = FULL_SERVER_PATH + "/history";
    public final static String GET_LOADED_PROGRAM = FULL_SERVER_PATH + "/loadedProgram";
    public final static String GET_INPUTS_NAMES = FULL_SERVER_PATH + "/programInputsNames";
    public final static String RUN_PROGRAM = FULL_SERVER_PATH + "/runProgram";

}
