
package com.kuliza.lending.journey.automation;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Service
public class BilScoringTest {

	@Value(value="${outputFilePath.bil}")
	private String outputFilePath;

	@Value(value="${journeyUrl}")
	private String journeyUrl;


	private String getRequestBodyForValidateOtp(String mobileNumber) {
		JSONObject map = new JSONObject();
		map.put("mobile", mobileNumber);
		map.put("otp", "1200");
		return map.toString();
	}

	private String getTokenFromMobileNumber(String mobileNumber) throws JSONException, UnirestException {
		String validateOtpUrl = journeyUrl + "journey-0.0.1-SNAPSHOT/otp/validate-otp";
		String requestPayload = getRequestBodyForValidateOtp(mobileNumber);
		Map<String, String> defaultHeaders = new HashMap<>();
		defaultHeaders.put("Content-Type", "application/json");
		HttpResponse<String> response = Unirest.post(validateOtpUrl).headers(defaultHeaders)
				.body(new JSONObject(requestPayload)).asString();
		JSONObject jsonObject = new JSONObject(response.getBody());
		String access_token = jsonObject.getJSONObject("data").getString("access_token");
		return access_token;
	}

	private Map<String, String> initiateJourney(String accessToken) throws UnirestException {
		Map<String, String> defaultHeaders = new HashMap<>();
		defaultHeaders.put("Authorization", "Bearer " + accessToken);
		String initiateJourneyUrl = journeyUrl + "journey-0.0.1-SNAPSHOT/lending/journey/initiate?journeyName=scbBilFlow";
		HttpResponse<String> response = Unirest.get(initiateJourneyUrl).headers(defaultHeaders).asString();
		JSONObject jsonObject = new JSONObject(response.getBody());
		String processInstanceId = jsonObject.getJSONObject("data").getString("processInstanceId");
		String taskId = jsonObject.getJSONObject("data").getString("taskId");
		Map<String, String> Ids = new HashMap<String, String>();
		Ids.put("processInstanceId", processInstanceId);
		Ids.put("taskId", taskId);
		return Ids;
	}

	private JSONObject getFirstFormRequestBody(String processInstanceId, String taskId, String numOfGuarantors,
			BilPojo bilPojo) {
		JSONObject m = new JSONObject();
		m.put("processInstanceId", processInstanceId);
		m.put("taskId", taskId);
		JSONObject ch = new JSONObject();
		ch.put("numOfGuarantors", numOfGuarantors);
		ch.put("etbNtbFlag", bilPojo.getEtbNtbFlag());
		ch.put("businessVintage", bilPojo.getBusinessVintage());
		ch.put("industryType", bilPojo.getIndustryType());
		ch.put("numOpenHd", bilPojo.getNumOpenHd());
		ch.put("numOpenPr", bilPojo.getNumOpenPr());
		ch.put("totOpenMort", bilPojo.getTotOpenMort());
		m.put("formProperties", ch);
		return m;
	}

	private JSONObject getSecondFormRequestBody(String processInstanceId, String taskId, BilPojo bilPojo) {
		JSONObject m = new JSONObject();
		m.put("processInstanceId", processInstanceId);
		m.put("taskId", taskId);
		JSONObject ch = new JSONObject();
		ch.put("worstAgingL6mCc", bilPojo.getWorstAgingL6mCc());
		ch.put("worstAgingL6mOd", bilPojo.getWorstAgingL6mOd());
		ch.put("worstAgingL6mOt", bilPojo.getWorstAgingL6mOt());
		ch.put("numPartPayCcL3m", bilPojo.getNumPartPayCcL3m());
		ch.put("ciIvprMaxDelq", bilPojo.getCiIvprMaxDelq());
		ch.put("tue", bilPojo.getTue());
		ch.put("numEnquiriesCcL3m", bilPojo.getNumEnquiriesCcL3m());
		ch.put("numOpenUnsecPl", bilPojo.getNumOpenUnsecPl());
		ch.put("dti", bilPojo.getDti());
		ch.put("worstAgingL6mPl", bilPojo.getWorstAgingL6mPl());
		ch.put("riskGrade", bilPojo.getRiskGrade());
		ch.put("numFullPymtyL3m", bilPojo.getNumFullPymtyL3m());
		m.put("formProperties", ch);
		return m;
	}

	private JSONObject submitform(String accessToken, JSONObject jsonObject) throws UnirestException {
		Map<String, String> defaultHeaders = new HashMap<>();
		defaultHeaders.put("Content-Type", "application/json");
		defaultHeaders.put("Authorization", "Bearer " + accessToken);
		String submitUrl = journeyUrl + "journey-0.0.1-SNAPSHOT/lending/journey/submit-form";
		HttpResponse<String> response = Unirest.post(submitUrl).headers(defaultHeaders).body(jsonObject).asString();
		JSONObject jsonObjectResponse = new JSONObject(response.getBody());
		return jsonObjectResponse.getJSONObject("data");
	}

	@Async
	public void parseExcelForBil(MultipartFile file)
			throws EncryptedDocumentException, InvalidFormatException, IOException, JSONException {

		//String SAMPLE_XLSX_FILE_PATH = "/home/kuliza-459/Documents/devetb.xlsx";
		File excelFile = new File(file.getOriginalFilename());
		copyInputStreamToFile(file.getInputStream(), excelFile);
		Workbook workbook = WorkbookFactory.create(excelFile);

		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();
		BilPojo bil = null;
		Map<String, ArrayList<BilPojo>> map = new HashMap<String, ArrayList<BilPojo>>();
		int i = 0;
		for (Row row : sheet) {
			try {
				if (i < 1) {
					i++;
					continue;
				}
				if(dataFormatter.formatCellValue(row.getCell(0)).isEmpty())
					continue;
				bil = new BilPojo();
				bil.setMobileNo(dataFormatter.formatCellValue(row.getCell(0)));
				bil.setFields(dataFormatter.formatCellValue(row.getCell(1)));
				bil.setEtbNtbFlag(dataFormatter.formatCellValue(row.getCell(2)));
				bil.setBusinessVintage(dataFormatter.formatCellValue(row.getCell(3)));
				bil.setNumOpenHd(dataFormatter.formatCellValue(row.getCell(4)));
				bil.setTotOpenMort(dataFormatter.formatCellValue(row.getCell(5)));
				bil.setNumOpenPr(dataFormatter.formatCellValue(row.getCell(6)));
				bil.setIndustryType(dataFormatter.formatCellValue(row.getCell(7)));
				bil.setWorstAgingL6mCc(dataFormatter.formatCellValue(row.getCell(8)));
				bil.setWorstAgingL6mOd(dataFormatter.formatCellValue(row.getCell(9)));
				bil.setWorstAgingL6mOt(dataFormatter.formatCellValue(row.getCell(10)));
				bil.setWorstAgingL6mPl(dataFormatter.formatCellValue(row.getCell(11)));
				bil.setRiskGrade(dataFormatter.formatCellValue(row.getCell(12)));
				bil.setNumEnquiriesCcL3m(dataFormatter.formatCellValue(row.getCell(13)));
				bil.setNumOpenUnsecPl(dataFormatter.formatCellValue(row.getCell(14)));
				bil.setCiIvprMaxDelq(dataFormatter.formatCellValue(row.getCell(15)));
				bil.setNumPartPayCcL3m(dataFormatter.formatCellValue(row.getCell(16)));
				bil.setNumFullPymtyL3m(dataFormatter.formatCellValue(row.getCell(17)));
				bil.setDti(dataFormatter.formatCellValue(row.getCell(18)));
				bil.setTue(dataFormatter.formatCellValue(row.getCell(19)));
				bil.setLogOdds(dataFormatter.formatCellValue(row.getCell(20)));
				bil.setPd(dataFormatter.formatCellValue(row.getCell(21)));
				bil.setScaledScore(dataFormatter.formatCellValue(row.getCell(22)));
				bil.setCreditGrade(dataFormatter.formatCellValue(row.getCell(23)));
				bil.setSegment(dataFormatter.formatCellValue(row.getCell(24)));
				++i;
				map.putIfAbsent(bil.getFields(), new ArrayList<BilPojo>());
				map.get(bil.getFields()).add(bil);
			} catch (Exception e) {
				e.printStackTrace();
			}
			workbook.close();
		}
		//String OutputExcelFilePath = "/home/kuliza-459/Documents/changeEtb1.xlsx";
		HSSFWorkbook workbookOutput = new HSSFWorkbook();
		HSSFSheet outputSheet = createExcelSheet(workbookOutput);
		int j = 1;
		JSONArray jsonArray = new JSONArray();
		for (String key : map.keySet()) {
			try{
				int numOfGuarantors = map.get(key).size();
				if (numOfGuarantors == 1) {
					BilPojo bilPojo = map.get(key).get(0);
					System.out.println(bilPojo.getMobileNo());
					String accessToken = getTokenFromMobileNumber(bilPojo.getMobileNo());
					Map<String, String> ids = initiateJourney(accessToken);
					JSONObject resp = submitform(accessToken, getFirstFormRequestBody(ids.get("processInstanceId"),
							ids.get("taskId"), String.valueOf(numOfGuarantors), bilPojo));
					JSONObject resp1 = submitform(accessToken, getSecondFormRequestBody(resp.getString("processInstanceId"),
							resp.getString("taskId"), bilPojo));
					JSONObject outputJson = resp1.getJSONObject("etbOutput");
					String logOddsEtb = outputJson.getJSONObject("logOddsEtb").get("value").toString();
					String BILScoringETB = outputJson.getJSONObject("BILScoringETB").get("value").toString();
					String pdEtb = outputJson.getJSONObject("pdEtb").get("value").toString();
					String segmentEtb = outputJson.getJSONObject("segmentEtb").get("value").toString();
					String creditGrade = outputJson.getJSONObject("creditGrade").get("value").toString();
					writeDataInExelSheet(outputSheet, j++, ids.get("processInstanceId"), bilPojo.getMobileNo(),
							logOddsEtb, BILScoringETB, pdEtb, creditGrade, segmentEtb,key,bilPojo.getLogOdds(),bilPojo.getPd(),bilPojo.getScaledScore(),bilPojo.getCreditGrade(),bilPojo
					.getSegment());
					jsonArray.put(resp1);
				} else {
					List<BilPojo> listbilPojo = map.get(key);
					System.out.println("mulitple  " + listbilPojo.get(0).getMobileNo());
					String accessToken = getTokenFromMobileNumber(listbilPojo.get(0).getMobileNo());
					Map<String, String> ids = initiateJourney(accessToken);
					JSONObject resp = submitform(accessToken, getFirstFormRequestBody(ids.get("processInstanceId"),
							ids.get("taskId"), String.valueOf(numOfGuarantors), listbilPojo.get(0)));
					for (BilPojo bilPojo : listbilPojo) {
						resp = submitform(accessToken, getSecondFormRequestBody(resp.getString("processInstanceId"),
								resp.getString("taskId"), bilPojo));
					}
					JSONObject outputJson = resp.getJSONObject("etbOutput");
					String logOddsEtb = outputJson.getJSONObject("logOddsEtb").get("value").toString();
					String BILScoringETB = outputJson.getJSONObject("BILScoringETB").get("value").toString();
					String pdEtb = outputJson.getJSONObject("pdEtb").get("value").toString();
					String segmentEtb = outputJson.getJSONObject("segmentEtb").get("value").toString();
					String creditGrade = outputJson.getJSONObject("creditGrade").get("value").toString();
					writeDataInExelSheet(outputSheet, j++, ids.get("processInstanceId"), listbilPojo.get(0).getMobileNo(),
							logOddsEtb, BILScoringETB, pdEtb, creditGrade, segmentEtb,key,listbilPojo.get(0).getLogOdds(),listbilPojo.get(0).getPd(),listbilPojo.get(0).getScaledScore(),listbilPojo.get(0).getCreditGrade(), listbilPojo.get(0).getSegment());
					jsonArray.put(resp);
				}

			} catch (Exception e){
				e.printStackTrace();
			}
		}
		String fileName = outputFilePath + LocalDateTime.now() + ".xlsx";
		FileOutputStream fileOut = new FileOutputStream(fileName);
		workbookOutput.write(fileOut);
		fileOut.close();
	}


	private void writeDataInExelSheet(HSSFSheet outputSheet, int excelRow, String processInstanceId, String mobileNo,
			String logOddsSumNtb, String BilScoringNTB, String pdNtb, String creditGrade, String segmentNtb ,String field,String outLogsOdds,String outPd,String outScaledScore,String outCreditGrade, String segment) {
		HSSFRow row = outputSheet.createRow(excelRow);
		row.createCell(0).setCellValue(processInstanceId);
		row.createCell(1).setCellValue(mobileNo);
		row.createCell(2).setCellValue(field);
		row.createCell(3).setCellValue(logOddsSumNtb);
		row.createCell(4).setCellValue(BilScoringNTB);
		row.createCell(5).setCellValue(pdNtb);
		row.createCell(6).setCellValue(creditGrade);
		row.createCell(7).setCellValue(segmentNtb);
		row.createCell(8).setCellValue(outLogsOdds);
		row.createCell(9).setCellValue(outPd);
		row.createCell(10).setCellValue(outScaledScore);
		row.createCell(11).setCellValue(outCreditGrade);
		row.createCell(12).setCellValue(segment);

	}

	private HSSFSheet createExcelSheet(HSSFWorkbook workbook) {
		HSSFSheet sheet = workbook.createSheet("Scoring Output");
		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("processInstanceId");
		rowhead.createCell(1).setCellValue("getMobileNo");
		rowhead.createCell(2).setCellValue("fieldId");
		rowhead.createCell(3).setCellValue("logOddsEtb");
		rowhead.createCell(4).setCellValue("BILScoringETB");
		rowhead.createCell(5).setCellValue("pdEtb");
		rowhead.createCell(6).setCellValue("creditGrade");
		rowhead.createCell(7).setCellValue("segmentEtb");
		rowhead.createCell(8).setCellValue("outLogsOdds");
		rowhead.createCell(9).setCellValue("outPd");
		rowhead.createCell(10).setCellValue("outScaledScore");
		rowhead.createCell(11).setCellValue("outCreditGrade");
		rowhead.createCell(12).setCellValue("segment");
		return sheet;
	}

	private static void copyInputStreamToFile(InputStream inputStream, File file)
			throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int read;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		}

	}

}
