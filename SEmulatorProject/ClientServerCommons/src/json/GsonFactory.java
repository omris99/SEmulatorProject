package json;

import clientserverdto.InstructionDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.deserialize.InstructionDTODeserializer;

public class GsonFactory {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(InstructionDTO.class, new InstructionDTODeserializer())
            .create();

    private GsonFactory() {}

    public static Gson getGson() {
        return gson;
    }
}
