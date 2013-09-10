package com.bootcamp.gattani.twitterapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
	
    public static String getJSONString(JSONObject jsonObject) {
        return jsonObject.toString();
    }

    protected static String getString(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static long getLong(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getLong(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    protected static int getInt(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getInt(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    protected static double getDouble(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getDouble(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    protected static boolean getBoolean(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getBoolean(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


}
