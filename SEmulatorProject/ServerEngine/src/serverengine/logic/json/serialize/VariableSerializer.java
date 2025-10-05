package serverengine.logic.json.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import serverengine.logic.model.argument.variable.Variable;

import java.lang.reflect.Type;

public class VariableSerializer implements JsonSerializer<Variable> {
    @Override
    public JsonElement serialize(Variable variable, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", variable.getType().toString());
        jsonObject.addProperty("number", variable.getNumber());
        jsonObject.addProperty("representation", variable.getRepresentation());
        return jsonObject;
    }
}
