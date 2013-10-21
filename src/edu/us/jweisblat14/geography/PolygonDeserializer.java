package edu.us.jweisblat14.geography;



import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import edu.us.jweisblat14.geography.Polygon.PolygonType;


public class PolygonDeserializer implements JsonDeserializer<Polygon> {

	@Override
	public Polygon deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
        PolygonType type = context.deserialize(jsonObject.get("type"), PolygonType.class);
        switch (type) {
            case Polygon:
                return context.deserialize(json, SimplePolygon.class);
            case MultiPolygon:
                return context.deserialize(json, MultiPolygon.class);
            default:
                throw new JsonParseException("Unrecognized shape type: " + type);
        }
	}

}
