package serverengine.logic.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import clientserverdto.InstructionDTO;
import serverengine.logic.json.deserialize.InstructionDTODeserializer;
import serverengine.logic.json.deserialize.VariableDeserializer;
import serverengine.logic.json.serialize.VariableSerializer;
import serverengine.logic.model.argument.variable.Variable;

public class GsonFactory {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Variable.class, new VariableSerializer())
            .registerTypeAdapter(Variable.class, new VariableDeserializer())
            .registerTypeAdapter(InstructionDTO.class, new InstructionDTODeserializer())
            .create();

    private GsonFactory() {}

    public static Gson getGson() {
        return gson;
    }
}
