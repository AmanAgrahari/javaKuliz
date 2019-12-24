package automation;



import com.kuliza.lending.common.utils.CommonHelperFunctions;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScbBilJourney {

    @Value(value="${outputFilePath.bil}")
    String outputFilePath;

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
        String initiateJourneyUrl = journeyUrl + "journey-0.0.1-SNAPSHOT/lending/journey/initiate?journeyName=scbBilFlow3";
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
                                               Model model) {
        JSONObject m = new JSONObject();
        m.put("processInstanceId", processInstanceId);
        m.put("taskId", taskId);
        JSONObject ch = new JSONObject();
        ch.put("numOfGuarantors", numOfGuarantors);
        ch.put("loanAmount_system_bil", model.getLoanAmount_system_bil());
        ch.put("entityType", model.getEntityType());
        ch.put("businessVintage", model.getBusinessVintage());
        ch.put("bilAmount_system_bil", model.getBilAmount());
        ch.put("loan_rls_bil", model.getLoan_rls_bil());
        ch.put("bod_ebbs_bil", model.getBod_ebbs_bil());
        ch.put("numOpenHd", model.getNumOpenHd());
        ch.put("sto", CommonHelperFunctions.getBigDecimalValue(model.getSto()));
        ch.put("numOpenPr", model.getNumOpenPr());
        ch.put("industryType", model.getIndustryType());
        ch.put("otherIncomeRelatedParty_user_bil", model.getRelatedPartyRLSLoan());
        ch.put("etbNtbFlag", model.getEtbNtbFlag());
        ch.put("totOpenMort", model.getTotOpenMort());
        ch.put("tueBorrowingEntity_user_bil", model.getTueBorrowingEntity_user_bil());
        ch.put("incomeFactor",model.getIncomeFactor());
        ch.put("wclAmount_system_bil",model.getWclAmount());
        ch.put("productType", model.getProductType());
        ch.put("otherIncomeRelatedParty_user_bil",model.getOtherIncomeRelatedParty_user_bil());
        m.put("formProperties", ch);
        return m;
    }

    private JSONObject getSecondFormRequestBody(String processInstanceId, String taskId, Model model) {
        JSONObject m = new JSONObject();
        m.put("processInstanceId", processInstanceId);
        m.put("taskId", taskId);
        JSONObject ch = new JSONObject();
        ch.put("worstAgingL6mOd", model.getWorstAgingL6mOd());
        ch.put("ciIvprMaxDelq", model.getCiIvprMaxDelq());
        ch.put("exposure_cccis_bil",model.getExposure_cccis_bil());
        ch.put("tue", model.getTue());
        ch.put("bod_ebbs_bil",model.getBod_ebbs_bil());
        ch.put("ccplGuarantor_ccms_bil",model.getCcplGuarantor_ccms_bil());
        ch.put("worstAgingL6mPl", model.getWorstAgingL6mPl());
        ch.put("worstAgingL6mCc", model.getWorstAgingL6mCc());
        ch.put("worstAgingL6mOt", model.getWorstAgingL6mOt());
        ch.put("numPartPayCcL3m", model.getNumPartPayCcL3m());
        ch.put("numEnquiriesCcL3m", model.getNumEnquiriesCcL3m());
        ch.put("numOpenUnsecPl", model.getNumOpenUnsecPl());
        ch.put("dti", model.getDti());
        ch.put("riskGrade", model.getRiskGrade());
        ch.put("numFullPymtyL3m", model.getNumFullPymtyL3m());
        ch.put("pclGuarantor_icm_bil",model.getPclGuarantor_icm_bil());
        m.put("formProperties", ch);
        return m;
    }

    private JSONObject getThirdFormRequestBody(String processInstanceId, String taskId){
        JSONObject m = new JSONObject();
        m.put("processInstanceId", processInstanceId);
        m.put("taskId", taskId);
        JSONObject ch = new JSONObject();
        m.put("formProperties", ch);
        return m;
    }
    private JSONObject submitform(String accessToken, JSONObject jsonObject) throws UnirestException {
        Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Authorization", "Bearer " + accessToken);
        String submitUrl = journeyUrl + "journey-0.0.1-SNAPSHOT/lending/journey/submit-form";
        System.out.println("Request Body submit Form:-"+jsonObject+" URL:-"+submitUrl);
        HttpResponse<String> response = Unirest.post(submitUrl).headers(defaultHeaders).body(jsonObject).asString();
        System.out.println("Response Body:-"+response.getBody());
        JSONObject jsonObjectResponse = new JSONObject(response.getBody());
        System.out.println("Json Object Submit Form:-"+jsonObjectResponse);
        return jsonObjectResponse.getJSONObject("data");
    }

    @Async
    public void parseExcelForBilEtb(MultipartFile file)
            throws EncryptedDocumentException, InvalidFormatException, IOException, JSONException {

       // String SAMPLE_XLSX_FILE_PATH = "/home/kuliza-459/Documents/journey.xlsx";
        File excelFile = new File(file.getOriginalFilename());
        copyInputStreamToFile(file.getInputStream(), excelFile);
        Workbook workbook = WorkbookFactory.create(excelFile);

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Model model = null;
        Map<String, ArrayList<Model>> map = new HashMap<String, ArrayList<Model>>();
        int i = 0;
        for (Row row : sheet) {
            try {
                if (i < 1) {
                    i++;
                    continue;
                }
                if(dataFormatter.formatCellValue(row.getCell(0)).isEmpty())
                    continue;
                model = new Model();
                model.setMobileNo(dataFormatter.formatCellValue(row.getCell(0)));
                model.setFields(dataFormatter.formatCellValue(row.getCell(1)));
                model.setEtbNtbFlag(dataFormatter.formatCellValue(row.getCell(2)));
                model.setBusinessVintage(dataFormatter.formatCellValue(row.getCell(3)));
                model.setNumOpenHd(dataFormatter.formatCellValue(row.getCell(4)));
                model.setTotOpenMort(dataFormatter.formatCellValue(row.getCell(5)));
                model.setNumOpenPr(dataFormatter.formatCellValue(row.getCell(6)));
                model.setIndustryType(dataFormatter.formatCellValue(row.getCell(7)));
                model.setWorstAgingL6mCc(dataFormatter.formatCellValue(row.getCell(8)));
                model.setWorstAgingL6mOd(dataFormatter.formatCellValue(row.getCell(9)));
                model.setWorstAgingL6mOt(dataFormatter.formatCellValue(row.getCell(10)));
                model.setWorstAgingL6mPl(dataFormatter.formatCellValue(row.getCell(11)));
                model.setRiskGrade(dataFormatter.formatCellValue(row.getCell(12)));
                model.setNumEnquiriesCcL3m(dataFormatter.formatCellValue(row.getCell(13)));
                model.setNumOpenUnsecPl(dataFormatter.formatCellValue(row.getCell(14)));
                model.setCiIvprMaxDelq(dataFormatter.formatCellValue(row.getCell(15)));
                model.setNumPartPayCcL3m(dataFormatter.formatCellValue(row.getCell(16)));
                model.setNumFullPymtyL3m(dataFormatter.formatCellValue(row.getCell(17)));
                model.setDti(dataFormatter.formatCellValue(row.getCell(18)));
                model.setTue(dataFormatter.formatCellValue(row.getCell(19)));
                model.setLoanAmount_system_bil(dataFormatter.formatCellValue(row.getCell(20)));
                model.setExposure_cccis_bil(dataFormatter.formatCellValue(row.getCell(21)));
                model.setEntityType(dataFormatter.formatCellValue(row.getCell(22)));
                model.setBod_ebbs_bil(dataFormatter.formatCellValue(row.getCell(23)));
                model.setLoan_rls_bil(dataFormatter.formatCellValue(row.getCell(24)));
                model.setCcplGuarantor_ccms_bil(dataFormatter.formatCellValue(row.getCell(25)));
                model.setTueBorrowingEntity_user_bil(dataFormatter.formatCellValue(row.getCell(26)));
                model.setIncomeFactor(dataFormatter.formatCellValue(row.getCell(27)));
                model.setPclGuarantor_icm_bil(dataFormatter.formatCellValue(row.getCell(28)));
                model.setProductType(dataFormatter.formatCellValue(row.getCell(29)));
                model.setSto(dataFormatter.formatCellValue(row.getCell(30)));
                model.setRelatedPartyRLSLoan(dataFormatter.formatCellValue(row.getCell(31)));
                model.setBilAmount(dataFormatter.formatCellValue(row.getCell(32)));
                model.setWclAmount(dataFormatter.formatCellValue(row.getCell(33)));
                model.setOtherIncomeRelatedParty_user_bil(dataFormatter.formatCellValue(row.getCell(34)));
                ++i;
                map.putIfAbsent(model.getFields(), new ArrayList<Model>());
                map.get(model.getFields()).add(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
            workbook.close();
        }
       // String OutputExcelFilePath = "/home/kuliza-459/Documents/journeyEtb.xlsx";
        HSSFWorkbook workbookOutput = new HSSFWorkbook();
        HSSFSheet outputSheet = createExcelSheet(workbookOutput);
        int j = 1;
        JSONArray jsonArray = new JSONArray();
        for (String key : map.keySet()) {
            try{
                int numOfGuarantors = map.get(key).size();
                if (numOfGuarantors == 1) {
                    Model model_1 = map.get(key).get(0);
                    System.out.println(model_1.getMobileNo());
                    String accessToken = getTokenFromMobileNumber(model_1.getMobileNo());
                    Map<String, String> ids = initiateJourney(accessToken);
                    JSONObject resp = submitform(accessToken, getFirstFormRequestBody(ids.get("processInstanceId"),
                            ids.get("taskId"), String.valueOf(numOfGuarantors), model_1));
                    System.out.println("Response After First Submit:-"+resp);
                    JSONObject resp1 = submitform(accessToken, getSecondFormRequestBody(resp.getString("processInstanceId"),
                            resp.getString("taskId"), model_1));
                    System.out.println("Response After Second Submit:-"+resp1);
                    JSONObject outputJson = resp1.getJSONObject("etbOutput");
                    String logOddsEtb = outputJson.getJSONObject("logOddsEtb").get("value").toString();
                    String BILScoringETB = outputJson.getJSONObject("BILScoringETB").get("value").toString();
                    String pdEtb = outputJson.getJSONObject("pdEtb").get("value").toString();
                    String segmentEtb = outputJson.getJSONObject("segmentEtb").get("value").toString();
                    String creditGrade = outputJson.getJSONObject("creditGrade").get("value").toString();

                    JSONObject resp2 = submitform(accessToken, getThirdFormRequestBody(resp1.getString("processInstanceId"),
                            resp1.getString("taskId")));
                    JSONObject loanAmountJson = resp2.getJSONObject("Loan");

                    String minBusinessIncome =  loanAmountJson.getJSONObject("minBusinessIncome").get("value").toString();
                    String loanAmount_system_bil =  loanAmountJson.getJSONObject("loanAmount_system_bil").get("value").toString();
                    String maxSto =  loanAmountJson.getJSONObject("maxSto").get("value").toString();
                    String creditGradeCheck =  loanAmountJson.getJSONObject("creditGradeCheck").get("value").toString();
                    String minSto =  loanAmountJson.getJSONObject("minSto").get("value").toString();
                    String tuePass =  loanAmountJson.getJSONObject("tuePass").get("value").toString();
                    String tueAmount =  loanAmountJson.getJSONObject("tueAmount").get("value").toString();
                    String mue =  loanAmountJson.getJSONObject("mue").get("value").toString();
                    String nar =  loanAmountJson.getJSONObject("nar").get("value").toString();

                    writeDataInExelSheet(outputSheet, j++, ids.get("processInstanceId"), model_1.getMobileNo(),
                            logOddsEtb, BILScoringETB, pdEtb, creditGrade, segmentEtb, key ,minBusinessIncome,loanAmount_system_bil,maxSto,creditGradeCheck,minSto,tuePass,tueAmount,mue,nar);
                    jsonArray.put(resp1);
                } else {
                    List<Model> listModelPojo = map.get(key);
                    System.out.println("mulitple  " + listModelPojo.get(0).getMobileNo());
                    String accessToken = getTokenFromMobileNumber(listModelPojo.get(0).getMobileNo());
                    Map<String, String> ids = initiateJourney(accessToken);
                    JSONObject resp = submitform(accessToken, getFirstFormRequestBody(ids.get("processInstanceId"),
                            ids.get("taskId"), String.valueOf(numOfGuarantors), listModelPojo.get(0)));
                    for (Model modelPojo : listModelPojo) {
                        resp = submitform(accessToken, getSecondFormRequestBody(resp.getString("processInstanceId"),
                                resp.getString("taskId"), modelPojo));
                    }
                    JSONObject outputJson = resp.getJSONObject("etbOutput");
                    String logOddsEtb = outputJson.getJSONObject("logOddsEtb").get("value").toString();
                    String BILScoringETB = outputJson.getJSONObject("BILScoringETB").get("value").toString();
                    String pdEtb = outputJson.getJSONObject("pdEtb").get("value").toString();
                    String segmentEtb = outputJson.getJSONObject("segmentEtb").get("value").toString();
                    String creditGrade = outputJson.getJSONObject("creditGrade").get("value").toString();
                    JSONObject resp2 = submitform(accessToken, getThirdFormRequestBody(resp.getString("processInstanceId"),
                            resp.getString("taskId")));
                    JSONObject loanAmountJson = resp2.getJSONObject("Loan");

                    String minBusinessIncome =  loanAmountJson.getJSONObject("minBusinessIncome").get("value").toString();
                    String loanAmount_system_bil =  loanAmountJson.getJSONObject("loanAmount_system_bil").get("value").toString();
                    String maxSto =  loanAmountJson.getJSONObject("maxSto").get("value").toString();
                    String creditGradeCheck =  loanAmountJson.getJSONObject("creditGradeCheck").get("value").toString();
                    String minSto =  loanAmountJson.getJSONObject("minSto").get("value").toString();
                    String tuePass =  loanAmountJson.getJSONObject("tuePass").get("value").toString();
                    String tueAmount =  loanAmountJson.getJSONObject("tueAmount").get("value").toString();
                    String mue =  loanAmountJson.getJSONObject("mue").get("value").toString();
                    String nar =  loanAmountJson.getJSONObject("nar").get("value").toString();

                    writeDataInExelSheet(outputSheet, j++, ids.get("processInstanceId"), listModelPojo.get(0).getMobileNo(),
                            logOddsEtb, BILScoringETB, pdEtb, creditGrade, segmentEtb,key,minBusinessIncome,loanAmount_system_bil,maxSto,creditGradeCheck,minSto,tuePass,tueAmount,mue,nar);
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
        rowhead.createCell(8).setCellValue("minBusinessIncome");
        rowhead.createCell(9).setCellValue("maxSto");
        rowhead.createCell(10).setCellValue("creditGradeCheck");
        rowhead.createCell(11).setCellValue("minSto");
        rowhead.createCell(12).setCellValue("tuePass");
        rowhead.createCell(13).setCellValue("tueAmount");
        rowhead.createCell(14).setCellValue("mue");
        rowhead.createCell(15).setCellValue("nar");
        rowhead.createCell(16).setCellValue("loanAmount_system_bil");
        return sheet;
    }


    private void writeDataInExelSheet(HSSFSheet outputSheet, int excelRow, String processInstanceId, String mobileNo,
                                      String logOddsEtb, String BILScoringETB, String creditGrade, String pdEtb, String segmentEtb,String field , String minBusinessIncome, String loanAmount_system_bil,String maxSto,String creditGradeCheck,String minSto,String tuePass,String tueAmount,String mue,String nar){
        HSSFRow row = outputSheet.createRow(excelRow);
        row.createCell(0).setCellValue(processInstanceId);
        row.createCell(1).setCellValue(mobileNo);
        row.createCell(2).setCellValue(field);
        row.createCell(3).setCellValue(logOddsEtb);
        row.createCell(4).setCellValue(BILScoringETB);
        row.createCell(5).setCellValue(pdEtb);
        row.createCell(6).setCellValue(creditGrade);
        row.createCell(7).setCellValue(segmentEtb);
        row.createCell(8).setCellValue(minBusinessIncome);
        row.createCell(9).setCellValue(maxSto);
        row.createCell(10).setCellValue(creditGradeCheck);
        row.createCell(11).setCellValue(minSto);
        row.createCell(12).setCellValue(tuePass);
        row.createCell(13).setCellValue(tueAmount);
        row.createCell(14).setCellValue(mue);
        row.createCell(15).setCellValue(nar);
        row.createCell(16).setCellValue(loanAmount_system_bil);
    }

    private JSONObject getSecondFormRequestBodyForNtb(String processInstanceId, String taskId, Model model) {
        JSONObject m = new JSONObject();
        m.put("processInstanceId", processInstanceId);
        m.put("taskId", taskId);
        JSONObject ch = new JSONObject();
        ch.put("numFullPymtyL6m", model.getNumFullPymtyL6m());
        ch.put("worstAgingL6mOd", model.getWorstAgingL6mOd());
        ch.put("exposure_cccis_bil", model.getExposure_cccis_bil());
        ch.put("tue", model.getTue());
        ch.put("ccplGuarantor_ccms_bil", model.getCcplGuarantor_ccms_bil());
        ch.put("totOpenMv", model.getTotOpenMv());
        ch.put("worstAgingL6mPl", model.getWorstAgingL6mPl());
        ch.put("numOpenHd", model.getNumOpenHd());
        ch.put("numCashAdvyL6m", model.getNumCashAdvyL6m());
        ch.put("numOpenMl", model.getNumOpenMl());
        ch.put("delqOffUsUnsecMob", model.getDelqOffUsUnsecMob());
        ch.put("worstAgingL6mCc", model.getWorstAgingL6mCc());
        ch.put("numOpenPr", model.getNumOpenPr());
        ch.put("worstAgingL6mOt", model.getWorstAgingL6mOt());
        ch.put("numOpenHl", model.getNumOpenHl());
        ch.put("numEnquiriesPlL3m", model.getNumEnquiriesPlL3m());
        ch.put("numPartPayCcL6m", model.getNumPartPayCcL6m());
        ch.put("dti", model.getDti());
        ch.put("totOpenMort", model.getTotOpenMort());
        ch.put("pclGuarantor_icm_bil", model.getPclGuarantor_icm_bil());
        m.put("formProperties", ch);
        return m;
    }

    @Async
    public void parseExcelForBilNtb(MultipartFile file)
            throws EncryptedDocumentException, InvalidFormatException, IOException, JSONException {
        //String SAMPLE_XLSX_FILE_PATH = "/home/kuliza-459/Documents/n1.xlsx";
        File excelFile = new File(file.getOriginalFilename());
        copyInputStreamToFile(file.getInputStream(), excelFile);
        Workbook workbook = WorkbookFactory.create(excelFile);

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Model model = null;
        Map<String, ArrayList<Model>> map = new HashMap<String, ArrayList<Model>>();
        int i = 0;
        for (Row row : sheet) {
            try {
                if (i < 1) {
                    i++;
                    continue;
                }
                model = new Model();
                if (dataFormatter.formatCellValue(row.getCell(0)).isEmpty())
                    continue;
                model.setMobileNo(dataFormatter.formatCellValue(row.getCell(0)));
                model.setFields(dataFormatter.formatCellValue(row.getCell(1)));
                model.setSto(dataFormatter.formatCellValue(row.getCell(2)));
                model.setEtbNtbFlag(dataFormatter.formatCellValue(row.getCell(3)));
                model.setBusinessVintage(dataFormatter.formatCellValue(row.getCell(4)));
                model.setIndustryType(dataFormatter.formatCellValue(row.getCell(5)));
                model.setNumEnquiriesPlL3m(dataFormatter.formatCellValue(row.getCell(6)));
                model.setWorstAgingL6mCc(dataFormatter.formatCellValue(row.getCell(7)));
                model.setWorstAgingL6mOd(dataFormatter.formatCellValue(row.getCell(8)));
                model.setWorstAgingL6mOt(dataFormatter.formatCellValue(row.getCell(9)));
                model.setWorstAgingL6mPl(dataFormatter.formatCellValue(row.getCell(10)));
                model.setNumOpenHd(dataFormatter.formatCellValue(row.getCell(11)));
                model.setTotOpenMv(dataFormatter.formatCellValue(row.getCell(12)));
                model.setNumOpenHl(dataFormatter.formatCellValue(row.getCell(13)));
                model.setNumOpenMl(dataFormatter.formatCellValue(row.getCell(14)));
                model.setTotOpenMort(dataFormatter.formatCellValue(row.getCell(15)));
                model.setNumOpenPr(dataFormatter.formatCellValue(row.getCell(16)));
                model.setDelqOffUsUnsecMob(dataFormatter.formatCellValue(row.getCell(17)));
                model.setNumPartPayCcL6m(dataFormatter.formatCellValue(row.getCell(18)));
                model.setNumFullPymtyL6m(dataFormatter.formatCellValue(row.getCell(19)));
                model.setNumCashAdvyL6m(dataFormatter.formatCellValue(row.getCell(20)));
                model.setDti(dataFormatter.formatCellValue(row.getCell(21)));
                model.setTue(dataFormatter.formatCellValue(row.getCell(22)));
                model.setLoanAmount_system_bil(dataFormatter.formatCellValue(row.getCell(23)));
                model.setExposure_cccis_bil(dataFormatter.formatCellValue(row.getCell(24)));
                model.setEntityType(dataFormatter.formatCellValue(row.getCell(25)));
                model.setBod_ebbs_bil(dataFormatter.formatCellValue(row.getCell(26)));
                model.setLoan_rls_bil(dataFormatter.formatCellValue(row.getCell(27)));
                model.setCcplGuarantor_ccms_bil(dataFormatter.formatCellValue(row.getCell(28)));
                model.setTueBorrowingEntity_user_bil(dataFormatter.formatCellValue(row.getCell(29)));
                model.setIncomeFactor(dataFormatter.formatCellValue(row.getCell(30)));
                model.setPclGuarantor_icm_bil(dataFormatter.formatCellValue(row.getCell(31)));
                model.setProductType(dataFormatter.formatCellValue(row.getCell(32)));
                model.setSto(dataFormatter.formatCellValue(row.getCell(33)));
                model.setRelatedPartyRLSLoan(dataFormatter.formatCellValue(row.getCell(34)));
                model.setBilAmount(dataFormatter.formatCellValue(row.getCell(35)));
                model.setWclAmount(dataFormatter.formatCellValue(row.getCell(36)));
                model.setOtherIncomeRelatedParty_user_bil(dataFormatter.formatCellValue(row.getCell(37)));
                ++i;
                map.putIfAbsent(model.getFields(), new ArrayList<Model>());
                map.get(model.getFields()).add(model);

            } catch (Exception e) {
                e.printStackTrace();
            }
            workbook.close();
        }
     //   String OutputExcelFilePath = "/home/kuliza-459/Documents/NtbCsv.csv";
        HSSFWorkbook workbookOutput = new HSSFWorkbook();
        HSSFSheet outputSheet = createExcelSheetNtb(workbookOutput);
        int j = 1;

        JSONArray jsonArray = new JSONArray();
        for (String key : map.keySet()) {
            try {
                int numOfGuarantors = map.get(key).size();
                if (numOfGuarantors == 1) {
                    Model model_1 = map.get(key).get(0);
                    System.out.println(model_1.getMobileNo());
                    String accessToken = getTokenFromMobileNumber(model_1.getMobileNo());
                    Map<String, String> ids = initiateJourney(accessToken);
                    JSONObject resp = submitform(accessToken, getFirstFormRequestBody(ids.get("processInstanceId"),
                            ids.get("taskId"), String.valueOf(numOfGuarantors), model_1));
                    JSONObject resp1 = submitform(accessToken, getSecondFormRequestBodyForNtb(resp.getString("processInstanceId"),
                            resp.getString("taskId"), model_1));

                    JSONObject outputJson = resp1.getJSONObject("ntbOutput");
                    String logOddsSumNtb = outputJson.getJSONObject("logOddsSumNtb").get("value").toString();
                    String BilScoringNTB = outputJson.getJSONObject("BilScoringNTB").get("value").toString();
                    String pdNtb = outputJson.getJSONObject("pdNtb").get("value").toString();
                    String segmentNtb = outputJson.getJSONObject("segmentNtb").get("value").toString();

                    JSONObject resp2 = submitform(accessToken, getThirdFormRequestBody(resp1.getString("processInstanceId"),
                            resp1.getString("taskId")));
                    JSONObject loanAmountJson = resp2.getJSONObject("Loan");
                    String minBusinessIncome =  loanAmountJson.getJSONObject("minBusinessIncome").get("value").toString();
                    String loanAmount_system_bil =  loanAmountJson.getJSONObject("loanAmount_system_bil").get("value").toString();
                    String maxSto =  loanAmountJson.getJSONObject("maxSto").get("value").toString();
                    String creditGradeCheck =  loanAmountJson.getJSONObject("creditGradeCheck").get("value").toString();
                    String minSto =  loanAmountJson.getJSONObject("minSto").get("value").toString();
                    String tuePass =  loanAmountJson.getJSONObject("tuePass").get("value").toString();
                    String tueAmount =  loanAmountJson.getJSONObject("tueAmount").get("value").toString();
                    String mue =  loanAmountJson.getJSONObject("mue").get("value").toString();
                    String nar =  loanAmountJson.getJSONObject("nar").get("value").toString();

                    writeDataInExelSheetNtb(outputSheet, j++, ids.get("processInstanceId"), model_1.getMobileNo(),
                            logOddsSumNtb, BilScoringNTB, pdNtb, segmentNtb, key,minBusinessIncome,loanAmount_system_bil,maxSto,creditGradeCheck,minSto,tuePass,tueAmount,mue,nar);
                    jsonArray.put(resp1);
                } else {
                    List<Model> modelbilPojo = map.get(key);
                    System.out.println("mulitple  " + modelbilPojo.get(0).getMobileNo());
                    String accessToken = getTokenFromMobileNumber(modelbilPojo.get(0).getMobileNo());
                    Map<String, String> ids = initiateJourney(accessToken);
                    JSONObject resp = submitform(accessToken, getFirstFormRequestBody(ids.get("processInstanceId"),
                            ids.get("taskId"), String.valueOf(numOfGuarantors), modelbilPojo.get(0)));
                    for (Model model_1 : modelbilPojo) {
                        resp = submitform(accessToken, getSecondFormRequestBodyForNtb(resp.getString("processInstanceId"),
                                resp.getString("taskId"), model_1));
                    }
                    JSONObject outputJson = resp.getJSONObject("ntbOutput");
                    String logOddsSumNtb = outputJson.getJSONObject("logOddsSumNtb").get("value").toString();
                    String BilScoringNTB = outputJson.getJSONObject("BilScoringNTB").get("value").toString();
                    String pdNtb = outputJson.getJSONObject("pdNtb").get("value").toString();
                    String segmentNtb = outputJson.getJSONObject("segmentNtb").get("value").toString();
                    JSONObject resp2 = submitform(accessToken, getThirdFormRequestBody(resp.getString("processInstanceId"),
                            resp.getString("taskId")));
                    JSONObject loanAmountJson = resp2.getJSONObject("Loan");
                    String minBusinessIncome =  loanAmountJson.getJSONObject("minBusinessIncome").get("value").toString();
                    String loanAmount_system_bil =  loanAmountJson.getJSONObject("loanAmount_system_bil").get("value").toString();
                    String maxSto =  loanAmountJson.getJSONObject("maxSto").get("value").toString();
                    String creditGradeCheck =  loanAmountJson.getJSONObject("creditGradeCheck").get("value").toString();
                    String minSto =  loanAmountJson.getJSONObject("minSto").get("value").toString();
                    String tuePass =  loanAmountJson.getJSONObject("tuePass").get("value").toString();
                    String tueAmount =  loanAmountJson.getJSONObject("tueAmount").get("value").toString();
                    String mue =  loanAmountJson.getJSONObject("mue").get("value").toString();
                    String nar =  loanAmountJson.getJSONObject("nar").get("value").toString();



                    writeDataInExelSheetNtb(outputSheet, j++, ids.get("processInstanceId"), modelbilPojo.get(0).getMobileNo(),
                            logOddsSumNtb, BilScoringNTB, pdNtb, segmentNtb,key,minBusinessIncome,loanAmount_system_bil,maxSto,creditGradeCheck,minSto,tuePass,tueAmount,mue,nar);
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

    private HSSFSheet createExcelSheetNtb(HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.createSheet("Scoring Output");
        HSSFRow rowhead = sheet.createRow((short) 0);
        rowhead.createCell(0).setCellValue("processInstanceId");
        rowhead.createCell(1).setCellValue("getMobileNo");
        rowhead.createCell(2).setCellValue("fieldId");
        rowhead.createCell(3).setCellValue("logOddsSumNtb");
        rowhead.createCell(4).setCellValue("BilScoringNTB");
        rowhead.createCell(5).setCellValue("pdNtb");
        rowhead.createCell(6).setCellValue("segmentNtb");
        rowhead.createCell(7).setCellValue("minBusinessIncome");
        rowhead.createCell(8).setCellValue("maxSto");
        rowhead.createCell(9).setCellValue("creditGradeCheck");
        rowhead.createCell(10).setCellValue("minSto");
        rowhead.createCell(11).setCellValue("tuePass");
        rowhead.createCell(12).setCellValue("tueAmount");
        rowhead.createCell(13).setCellValue("mue");
        rowhead.createCell(14).setCellValue("nar");
        rowhead.createCell(15).setCellValue("loanAmount_system_bil");
        return sheet;
    }
    private void writeDataInExelSheetNtb(HSSFSheet outputSheet, int excelRow, String processInstanceId, String mobileNo,
                                      String logOddsSumNtb, String BilScoringNTB, String pdNtb, String segmentNtb ,String field,
                                         String minBusinessIncome, String loanAmount_system_bil,String maxSto,String creditGradeCheck,String minSto,String tuePass,String tueAmount,String mue,String nar) {
        HSSFRow row = outputSheet.createRow(excelRow);
        row.createCell(0).setCellValue(processInstanceId);
        row.createCell(1).setCellValue(mobileNo);
        row.createCell(2).setCellValue(field);
        row.createCell(3).setCellValue(logOddsSumNtb);
        row.createCell(4).setCellValue(BilScoringNTB);
        row.createCell(5).setCellValue(pdNtb);
        row.createCell(6).setCellValue(segmentNtb);
        row.createCell(7).setCellValue(minBusinessIncome);
        row.createCell(8).setCellValue(maxSto);
        row.createCell(9).setCellValue(creditGradeCheck);
        row.createCell(10).setCellValue(minSto);
        row.createCell(11).setCellValue(tuePass);
        row.createCell(12).setCellValue(tueAmount);
        row.createCell(13).setCellValue(mue);
        row.createCell(14).setCellValue(nar);
        row.createCell(15).setCellValue(loanAmount_system_bil);
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
