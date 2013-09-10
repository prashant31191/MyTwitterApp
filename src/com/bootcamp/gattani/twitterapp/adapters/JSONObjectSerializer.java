package com.bootcamp.gattani.twitterapp.adapters;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.serializer.TypeSerializer;

final public class JSONObjectSerializer extends TypeSerializer {
	@Override
	public Class<?> getDeserializedType() {
		return JSONObject.class;
	}

	@Override
	public SerializedType getSerializedType() {
		return SerializedType.STRING;
	}

	@Override
	public String serialize(Object data) {
		if (data == null) {
			return null;
		}

		return ((JSONObject)data).toString();
	}

	@Override
	public JSONObject deserialize(Object data) {
		if (data == null) {
			return null;
		}

		try {
			return new JSONObject((String)data);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}