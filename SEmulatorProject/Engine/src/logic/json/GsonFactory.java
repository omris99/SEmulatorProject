package logic.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import dto.InstructionDTO;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;

import java.lang.reflect.Type;

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
