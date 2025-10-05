package serverengine.logic.json.deserialize;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import clientserverdto.InstructionDTO;

import java.lang.reflect.Type;
import java.util.List;

public class InstructionDTODeserializer implements JsonDeserializer<InstructionDTO> {
    @Override
    public InstructionDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int index = jsonObject.get("index").getAsInt();
        String instructionType = jsonObject.get("instructionType").getAsString();
        String label = jsonObject.get("label").getAsString();
        String displayFormat = jsonObject.get("displayFormat").getAsString();
        int cycles = jsonObject.get("cycles").getAsInt();
        boolean isBreakpointSet = jsonObject.get("isBreakpointSet").getAsBoolean();

        List<InstructionDTO> parentInstructions = context.deserialize(
                jsonObject.get("parentInstructions"), new TypeToken<List<InstructionDTO>>() {}.getType()
        );

        List<String> associatedArgumentsAndLabels = context.deserialize(
                jsonObject.get("associatedArgumentsAndLabels"), new TypeToken<List<String>>() {}.getType()
        );

        return new InstructionDTO(index, instructionType, label, displayFormat, cycles, parentInstructions, associatedArgumentsAndLabels, isBreakpointSet);
    }
}
