package com.basics.help.java;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App {

	public static final String S = "uuu";

	public static void main(String[] args) throws JSONException, JsonParseException, JsonMappingException, IOException {

		String s = "{\n" + "  \"0\": {\n" + "    \"numberEnquiriesPlL3m\": 3,\n" + "    \"numFullPymtyL6m\": 3,\n"
				+ "    \"worstAgingL6mOd\": 3,\n" + "    \"tue\": 3,\n" + "    \"numberOpenMl\": 3,\n"
				+ "    \"totOpenMv\": \"AA\",\n" + "    \"worstAgingL6mPl\": 3,\n" + "    \"numOpenHd\": 3,\n"
				+ "    \"numCashAdvyL6m\": 3,\n" + "    \"sto\": 3,\n" + "    \"delqOffUsUnsecMob\": 3,\n"
				+ "    \"worstAgingL6mCc\": 3,\n" + "    \"numOpenPr\": 3,\n" + "    \"worstAgingL6mOt\": 3,\n"
				+ "    \"industryType\": \"AAAA\",\n" + "    \"numOpenHl\": 3,\n" + "    \"numPartPayCcL6m\": 3,\n"
				+ "    \"dti\": 3,\n" + "    \"totOpenMort\": 3,\n" + "  }\n" + "}";

		// System.out.println("" + s);

		JSONObject processVariable = new JSONObject(s);
		ObjectMapper obj = new ObjectMapper();
		List<WorkFlowModel> list = new ArrayList<WorkFlowModel>();

		@SuppressWarnings("rawtypes")
		Iterator keys = processVariable.keys();
		while (keys.hasNext()) {
			WorkFlowModel readValue = obj.readValue((processVariable.get(keys.next().toString()).toString()),
					WorkFlowModel.class);
			list.add(readValue);
		}

	}

	public static Boolean getBooleanValue(Object s) {
		return s != null && s.toString().toLowerCase().matches("^(true)|(false)$") ? Boolean.parseBoolean(s.toString())
				: false;
	}

	public static String getStringValue(Object s) {
		return s != null ? s.toString() : "";
	}

	public static long getDateDiffInMonths(String fromDate, LocalDate toDate) {
		long months = 0;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate fromToDate = LocalDate.parse(fromDate, formatter);
			months = ChronoUnit.MONTHS.between(fromToDate, toDate);
		} catch (DateTimeParseException ex) {
		}
		return months;
	}
}

