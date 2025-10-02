package logic.json.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;

import java.lang.reflect.Type;

public class VariableDeserializer implements JsonDeserializer<Variable> {
    @Override
    public Variable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        System.out.println("i am here");
        JsonElement typeElement = json.getAsJsonObject().get("type");
        VariableType type = VariableType.valueOf(json.getAsJsonObject().get("type").getAsString());
        int number = json.getAsJsonObject().get("number").getAsInt();
        return new VariableImpl(type, number);
    }
}
