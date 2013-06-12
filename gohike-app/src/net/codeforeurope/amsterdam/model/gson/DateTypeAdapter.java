package net.codeforeurope.amsterdam.model.gson;

import java.lang.reflect.Type;
import java.util.Date;

import net.codeforeurope.amsterdam.util.ApiConstants;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateTypeAdapter implements JsonSerializer<Date> {

	@Override
	public JsonElement serialize(Date date, Type type,
			JsonSerializationContext serializationContext) {
		String value = ApiConstants.DATE_FORMAT.format(date);
		JsonElement element = new JsonPrimitive(value);
		return element;
	}

}
