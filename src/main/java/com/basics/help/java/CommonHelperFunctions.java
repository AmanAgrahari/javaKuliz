package com.basics.help.java;



import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.HashMap;

public class CommonHelperFunctions {

	public CommonHelperFunctions() {
		throw new UnsupportedOperationException();
	}

	// function to generate log string
	public static String generateLogString(String method, int statusCode, String url, String userId, Object data) {
		StringBuilder logString = new StringBuilder();
		logString.append(method.concat(" "));
		logString.append(Integer.toString(statusCode).concat(" "));
		logString.append(url.concat(" "));
		logString.append("UserId : ".concat(userId).concat(" "));
		logString.append("Data : ".concat(data.toString()));
		return logString.toString();
	}

	// function to generate log string
	public static String generateLogString(String method, String url, String userId, Object data) {
		StringBuilder logString = new StringBuilder();
		logString.append(method.concat(" "));
		logString.append(url.concat(" "));
		logString.append("UserId : ".concat(userId).concat(" "));
		logString.append("Data : ".concat(data.toString()));
		return logString.toString();
	}


	// function to get stack trace for exception
	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	public static String getStringValue(Object s) {
		return s != null ? s.toString() : "";
	}

	public static String getStringValueOrEmptyArrayString(Object s) {
		return s != null ? s.toString() : "[]";
	}

	public static String getStringValueOrEmptyMapString(Object s) {
		return s != null ? s.toString() : "{}";
	}

	public static String getStringValueOrDefault(Object s, String defaultString) {
		return s != null ? s.toString() : defaultString;
	}

	public static Long getLongValue(Object s) {
		try {
			return s != null ? (long) Double.parseDouble(s.toString()) : new Long(0);
		} catch (NumberFormatException e) {
			return new Long(0);
		}
	}

	public static Integer getIntegerValue(Object s) {
		try {
			return s != null ? (int) Double.parseDouble(s.toString()) : 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static Double getDoubleValue(Object s) {
		try {
			return s != null ? Double.parseDouble(s.toString()) : new Double(0);
		} catch (NumberFormatException e) {
			return new Double(0);
		}

	}

	public static BigDecimal getBigDecimalValue(Object s) {
		try {
			return s != null ? new BigDecimal(s.toString()) : new BigDecimal("0");
		} catch (NumberFormatException e) {
			return new BigDecimal("0");
		}
	}

	public static Boolean getBooleanValue(Object s) {
		return s != null && s.toString().toLowerCase().matches("^(true)|(false)$") ? Boolean.parseBoolean(s.toString())
				: false;
	}

	public static final String generateUUIDTransanctionId() {
		return UUID.randomUUID().toString();
	}

	public static final ZonedDateTime getCurrentDateTime() {
		return ZonedDateTime.now(ZoneOffset.UTC);
	}

	public static final String getCurrentDateInFormat(String format) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String strDate = formatter.format(date);
		return strDate;
	}

	public static final String getDateInFormat(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String strDate = formatter.format(date);
		return strDate;
	}


	public static Map<String, Object> getMap(JSONObject jsonObject, List<String> keys) {

		Map<String, Object> map = new HashMap<>();
		for (String key : keys)
			map.put(key, jsonObject.get(key));
		return map;
	}

	public static Map<String, Object> getMap(Map<String, Object> jsonObject, List<String> keys) {

		Map<String, Object> map = new HashMap<>();
		for (String key : keys)
			if (jsonObject.containsKey(key)) {
				map.put(key, jsonObject.get(key));
			}
		return map;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isBigInt(String s) {
		try {
			new BigInteger(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isBoolean(String s) {
		if (s.toLowerCase().equals("false") || getStringValue(s).toLowerCase().equals("true")) {
			return true;
		}
		return false;

	}

	// get variable type credit engine
	public static Map<String, Object> getVariableType(Map.Entry<String, Object> entry) {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		if (isInteger(getStringValue(entry.getValue()))) {
			variableMap.put("key", entry.getKey() + "-Numerical");
			variableMap.put("value", getIntegerValue(entry.getValue().toString()));
			return variableMap;
		} else if (isBigInt(getStringValue(entry.getValue()))) {
			variableMap.put("key", entry.getKey() + "-Numerical");
			variableMap.put("value", new BigInteger(entry.getValue().toString()));
			return variableMap;
		} else if (isFloat(getStringValue(entry.getValue()))) {
			variableMap.put("key", entry.getKey() + "-Numerical");
			variableMap.put("value", Float.parseFloat(entry.getValue().toString()));
			return variableMap;
		} else if (isBoolean(getStringValue(entry.getValue()))) {
			variableMap.put("key", entry.getKey() + "-Boolean");
			variableMap.put("value", getBooleanValue(entry.getValue()));
			return variableMap;
		} else {
			variableMap.put("key", entry.getKey() + "-String");
			variableMap.put("value", entry.getValue().toString());
			return variableMap;
		}
	}

	// validate if a string is json string
	public static boolean isJSONValid(String jsonInString) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonInString);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static Map<String, Object> jsonStringToMap(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static Long getIdFromUserName(String userName) {
		Long id = null;
		id = getLongValue(userName.replaceAll("[^0-9]", ""));
		return id;
	}

	/**
	 * @param list      of string
	 * @param seperator like "," ,"@"
	 * @return string wiht seperator
	 */
	public static String getStringFromListWithSeperator(List<String> list, String seperator) {
		StringBuffer buffer = new StringBuffer();
		String defaultSeperator = ",";
		seperator = (seperator != null) ? (seperator.trim().equals("") ? defaultSeperator : seperator)
				: defaultSeperator;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
			String string = (String) iterator.next();
			buffer.append(string);
			buffer.append(seperator);
		}
		return (buffer.length() > 0) ? buffer.toString().substring(0, buffer.length() - 1) : buffer.toString();
	}

	public static List<String> GetRolesInList(List<String> rolesList) {
		List<String> roles = new ArrayList<String>();
		for (String roleName : rolesList) {
			if (roleName.startsWith("ROLE_")) {
				roleName = roleName.substring(5);
				roles.add(roleName);
			}
		}
		return roles;
	}


	/**
	 * This is an utility method used to check if a given string is not null and
	 * empty
	 *
	 * @param value
	 * @return true if String is not empty else false
	 */
	public static boolean isNotEmpty(String value) {
		return (value != null) && (!value.trim().equals(""));
	}

	public static String getJsonString(Map<String, Object> jsonInput) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return getStringValue(gson.toJson(jsonInput));
	}

	public static HashMap<String, Object> getHashMapFromJsonString(String jsonString) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			ObjectMapper mapper = new ObjectMapper();
			// convert JSON string to Map
			if (jsonString != null) {
				map = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
				});
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static JSONObject toJson(Map<String, Object> map) {
		JSONObject jsonObject = new JSONObject();

		for (String key : map.keySet()) {
			try {
				Object obj = map.get(key);
				if (obj instanceof Map) {
					jsonObject.put(key, toJson((Map) obj));
				} else if (obj instanceof List) {
					jsonObject.put(key, toJson((List) obj));
				} else {
					jsonObject.put(key, map.get(key));
				}
			} catch (JSONException jsone) {
				// Log.wtf("RequestManager", "Failed to put value for " + key + " into JSONObject.", jsone);
			}
		}

		return jsonObject;
	}

	public static JSONObject mapToJson(Map<String, String[]> map) {
		JSONObject jsonObject = new JSONObject();

		for (String key : map.keySet()) {
			try {
				String[] obj = map.get(key);
				JSONArray jsonArray = new JSONArray();
				for (String value : obj) {
					jsonArray.put(value);
				}
				jsonObject.put(key, jsonArray);
			} catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}

		return jsonObject;
	}

	public static JsonObject toJsonObject(Object object) {
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(getStringValue(object)).getAsJsonObject();
		return obj;
	}

	public static JSONArray toJson(List<Object> list) {
		JSONArray jsonArray = new JSONArray();

		for (Object obj : list) {
			if (obj instanceof Map) {
				jsonArray.put(toJson((Map) obj));
			} else if (obj instanceof List) {
				jsonArray.put(toJson((List) obj));
			} else {
				jsonArray.put(obj);
			}
		}

		return jsonArray;
	}

	public static Map<String, Object> fromJson(JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keyIterator = jsonObject.keys();
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			try {
				Object obj = jsonObject.get(key);

				if (obj instanceof JSONObject) {
					map.put(key, fromJson((JSONObject) obj));
				} else if (obj instanceof JSONArray) {
					map.put(key, fromJson((JSONArray) obj));
				} else {
					map.put(key, obj);
				}
			} catch (JSONException jsone) {
				// Log.wtf("RequestManager", "Failed to get value for " + key + " from JSONObject.", jsone);
			}
		}

		return map;
	}

	public static List<Object> fromJson(JSONArray jsonArray) {
		List<Object> list = new ArrayList<Object>();

		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				Object obj = jsonArray.get(i);

				if (obj instanceof JSONObject) {
					list.add(fromJson((JSONObject) obj));
				} else if (obj instanceof JSONArray) {
					list.add(fromJson((JSONArray) obj));
				} else {
					list.add(obj);
				}
			} catch (JSONException jsone) {
				// Log.wtf("RequestManager", "Failed to get value at index " + i + " from JSONArray.", jsone);
			}
		}

		return list;
	}

	public static String getJsonString(Object jsonInput) {
		return getJsonString((Map<String, Object>) jsonInput);
	}

	public static String getCamelCase(final String init) {
		if (init == null)
			return null;

		final StringBuilder ret = new StringBuilder(init.length());

		for (final String word : init.split(" ")) {
			if (!word.isEmpty()) {
				ret.append(Character.toUpperCase(word.charAt(0)));
				ret.append(word.substring(1).toLowerCase());
			}
			if (!(ret.length() == init.length()))
				ret.append(" ");
		}

		return ret.toString();
	}

	public static String getSubStringAfterLastOccurrenceOfRegex(String str, String regex) {
		return str != null && !str.isEmpty() ? str.substring(str.lastIndexOf(regex) + 1) : "";
	}

	public static String getDateStringValue(Date date, String formate) {
		DateFormat dateFormat = new SimpleDateFormat(formate);
		dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		return dateFormat.format(date);
	}

	public static File convertMultipartFiletoFile(MultipartFile multipartFile) throws IOException {
		File convFile = new File(multipartFile.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(multipartFile.getBytes());
		fos.close();
		return convFile;
	}

	public static JSONArray objectToJSONArray(Object object) {
		Object json = null;
		JSONArray jsonArray = new JSONArray();
		try {
			json = new JSONTokener(CommonHelperFunctions.getStringValue(object)).nextValue();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (json instanceof JSONArray) {
			jsonArray = (JSONArray) json;
		}
		return jsonArray;
	}


	public static Map<String, Object> getMapFromKeySet(List<String> keySet, Object defaultValue) {
		Map<String, Object> map = new HashMap<>();
		for (String key : keySet) {
			map.put(key, defaultValue);
		}
		return map;
	}

	/* To decode base64 string to actual String
	 *  @param: stringToBeEncoded
	 * */
	public static String decodeBase64EncodedString(String stringToBeDecoded) {
			return new String(Base64.decodeBase64(stringToBeDecoded));
	}

	/* To encode base64 string to decoded String
	 *  @param: stringToBeEncoded
	 * */
	public static String encodeBase64DecodedString(String stringToBeEncoded) {
		return new String(Base64.encodeBase64(stringToBeEncoded.getBytes()));
	}

	public static boolean isValidRegex(String string, String regex) {
		return (Pattern.matches(regex, string));
	}

	/* To get key from the hash map based on the given key
	* @param: map
	* @param: value
    */
	public static Object getKeyFromHashMap(Map<Object, Object> map, Object value) {
		HashBiMap<Object, Object> biMap = (HashBiMap<Object, Object>) map;
		return biMap.inverse().get(value);
	}

	/* To get key from the hash map based on the given key
	 * @param: map
	 * @param: value
	 */
	public static HashBiMap<String, Object> getBiHashMapFromMap(Map<String, Object> map, Object value) {
		HashBiMap<String, Object> biMap = (HashBiMap<String, Object>) map;
		return biMap;
	}

	/* To get key from the hash map based on the given key
	 * @param: map
	 * @param: value
	 */
	public static String getKeyInStringFromHashMap(Map<String, Object> map, Object value) {
		HashBiMap<String, Object> biMap = (HashBiMap<String, Object>) map;
		return biMap.inverse().get(value);
	}
}
