
public class services {
	private void psvm() {
		// TODO Auto-generated method stub

	}
	public Map<String,Object> downloadFile(String id) throws IOException, UnirestException {
        String url = HelperFunctions.getDMSUrl() + "?document_id=" + id;
        Map<String, String> headers = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        headers.put("Content-Type", "application/json");
        byte[] bytes = null;
        try {
            HttpResponse response = Unirest.get(url).headers(headers).asBinary();
            bytes = IOUtils.toByteArray(response.getRawBody());
            map.put("bytes",bytes);
            map.put("Content-Type",CommonHelperFunctions.getStringValue(response.getHeaders().getFirst("Content-Type")));
        } catch (Exception exception) {
            logger.warn("==========downloadFile==========exception : " + exception);
            throw exception;
        }
        return map;
    }
	
	
	public DelegateExecution saveEcsPdf(DelegateExecution execution) throws UnirestException, FileNotFoundException {
		String token = internalApiCalls.getTokenForDpInfo();
		String entityNumber = CommonHelperFunctions.getStringValue(execution.getVariable("companyUenNumber_user_bil"));
		String companyNameOfEcs = CommonHelperFunctions.getStringValue(execution.getVariable("companyName_user_bil"));
		JSONObject jsonObject = internalApiCalls.ECS("ECSR", entityNumber, companyNameOfEcs, token);
		JSONArray data_EIS_ECS = new JSONArray(jsonObject.get("Dats").toString());
		String orderItem = data_EIS_ECS.getJSONObject(0).getJSONObject("OrdItm").get("ID").toString();
		String requestParam = "https://uat.questnet.sg/API/Data/v2/Order/"+ orderItem+ "/Result?";
		InputStream byteArrayInputStream=null;
        String file="";
        try {
			HttpResponse<String> response = Unirest.get(requestParam)
					.headers(defaultHeader(token)).asString();
			logger.info("CE-RESPONSE");
			logger.info("Response body {} ", response.getRawBody());
			byteArrayInputStream = response.getRawBody();
            byte[] bytes = IOUtils.toByteArray(byteArrayInputStream);
            file = new String(bytes);
		} catch (Exception e) {
			logger.info("ERROR in getting the token from Credit Engine");
			e.printStackTrace();
		}
		try {
			String filenetToken = internalApiCalls.filenetToken();
			String applicationId=execution.getVariable("applicationId").toString();
			String applicantName = CommonHelperFunctions.getStringValue(execution.getVariable("applicantName_user_bil"));
			String productType = CommonHelperFunctions.getStringValue(execution.getVariable("journeyType"));
			JSONObject response = internalApiCalls.setfilenetService(filenetToken,file,"pdf",entityNumber,applicationId,entityNumber,applicantName,productType);
			logger.info("Response body for Add Documents" + response);
				String processInstanceId = execution.getProcessInstanceId();
				runtimeService.setVariable(processInstanceId, JourneyConstants.DPINFO_DOC_ID, response.getString("docId"));
			}

		catch (Exception e){
			throw e;
		}
		return execution;
	}
	
	  public JSONObject setfilenetService(String token, String addFile, String fileType, String fileName, String applicationId, String uenNumber, String applicantName, String productType) throws FileNotFoundException, UnirestException {
	        String hashGenerationUrl = "";
	        Map<String, String> header = requestHeader(token);
	        Map<String, Object> payload = requestBodyPost(addFile,fileType,fileName,applicationId,uenNumber,applicantName,productType);
	        logger.info("File Net Add Document Request Body:{}",payload.toString());
	        try {
	            hashGenerationUrl = IB_HOST_URL + JourneyConstants.IB_API_URL + JourneyConstants.FILENET__INTEGRATION_SLUG + "/" +
	                    JourneyConstants.SCB_FILENET_ADD_DOCUMENT + "/" + JourneyConstants.COMAPANY_SLUG + "/" +
	                    HelperFunctions.getHash(JourneyConstants.FILENET__INTEGRATION_SLUG, JourneyConstants.SCB_FILENET_ADD_DOCUMENT, JourneyConstants.COMAPANY_SLUG);
	            logger.info("URL for File Net Add Document : {} ", hashGenerationUrl);
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.info("Error while generating URL for FileNet Add Document: {} ", hashGenerationUrl);
	        }
	        HttpResponse<String> response = Unirest.post(hashGenerationUrl).headers(header).body(new JSONObject(payload)).asString();
	        logger.info("Response body for FileNet Add Document" + response.getBody());
	        if (response.getStatus() != 200) {
	            logger.error("FileNet Add Document Error:{}",response.getBody());
	            throw new BpmnError("FileNet Add Document Backend Error");
	        } else {
	            logger.info("FileNet Add Document-RESPONSE : " + response.getBody());
	            return new JSONObject(response.getBody());
	        }
	    }
	  
	  private Map<String, Object> requestBodyPost(String file, String fileType, String fileName,String applicationId,String uenNumber,String applicantName,String productType)  {
	        String pattern = "dd/MM/yyyy";
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	        String date = simpleDateFormat.format(new Date());
	        Map<String, String> propertyValues = new HashMap<>();
	        propertyValues.put("Channel", "1");
	        propertyValues.put("CreatedID", "KULIZA");
	        propertyValues.put("TxRefNo", applicationId);
	        propertyValues.put("TxnBranch", "Changi Business Park");
//	        propertyValues.put("ProcessId", "");
//	        propertyValues.put("AccNo", "");
//	        propertyValues.put("LoanNo", "");
	        propertyValues.put("DocTitle", fileName);
	        propertyValues.put("CustName", applicantName);//give name
	        propertyValues.put("FormId", "9999");
	        propertyValues.put("DocType", fileType);
	        propertyValues.put("PageCount", "0");
	        propertyValues.put("Source", "Kuliza");
	        propertyValues.put("Status", "New");
	        propertyValues.put("BatchRefNo", applicationId);
	        propertyValues.put("OrgTxRefNo", applicationId);
	        propertyValues.put("CntryCode", "SG");
	        propertyValues.put("IsAppDoc", "true");
	        propertyValues.put("CreatedDate", date);
//	        propertyValues.put("SubTxnType", "");
//	        propertyValues.put("TransType", "");
	        propertyValues.put("CustSeg","BB");
//	        propertyValues.put("ProcessID",productType.equalsIgnoreCase("bilUserJourney")?"J050":"J049");
	        propertyValues.put("WFName","DCAPDISTWF02");
	        propertyValues.put("Priority","3");
	        propertyValues.put("DocValue",uenNumber);
//	        propertyValues.put("TxnRefNo","");

	        Map<String, Object> payload = new HashMap<>();
	        payload.put("osName", "RBOS");
	        payload.put("docClassName", "RTLDoc");
	        payload.put("propertyValues", propertyValues);
	        payload.put("fileName", fileName);
	        payload.put("docContent", file);
	        payload.put("folderPath", "/");
	        payload.put("contentType",fileType);
	        return payload;
	    }


}
