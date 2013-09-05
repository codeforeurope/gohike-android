package net.codeforeurope.amsterdam.model.gson;

import java.lang.reflect.Type;
import java.util.HashMap;

import net.codeforeurope.amsterdam.model.TranslatedString;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class TranslatedStringTypeAdapter implements
		JsonDeserializer<TranslatedString> {

	public TranslatedStringTypeAdapter() {

	}

	@Override
	public TranslatedString deserialize(JsonElement element, Type type,
			JsonDeserializationContext serializationContext)
			throws JsonParseException {
		TranslatedString translatedString;
		if (element.isJsonPrimitive()) {
			String value = element.getAsString();
			translatedString = new TranslatedString(value);
		} else {
			HashMap<String, String> translations = getAsHashMap(element
					.getAsJsonObject());
			translatedString = new TranslatedString(translations);
		}
		return translatedString;
	}

	private HashMap<String, String> getAsHashMap(JsonObject json) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (HashMap.Entry<String, JsonElement> entry : json.entrySet())
			map.put(entry.getKey(), entry.getValue().getAsString());
		return map;
	}
}
