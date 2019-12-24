package automation;


import com.kuliza.lending.common.pojo.ApiResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	BilScoringNtbTest bilScoringNtbTest;

	@Autowired
	BilScoringTest bilScoringTest;

	@Autowired
	ScbBilJourney scbBilJourney;

	@RequestMapping(method = RequestMethod.POST, value = "/etb")
	public ApiResponse testEtb(@RequestParam("file") MultipartFile file) throws UnirestException, IOException, InvalidFormatException {
		bilScoringTest.parseExcelForBil(file);
		return new ApiResponse(200,"Get the file from this location after some time","");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/ntb")
	public ApiResponse testNtb(@RequestParam("file") MultipartFile file) throws UnirestException, IOException, InvalidFormatException {
	bilScoringNtbTest.parseExcelForBilNtb(file);;
		return new ApiResponse(200,"Get the file from this location after some time","");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/bilScoringEtb")
	public ApiResponse testBilScoringEtb(@RequestParam("file") MultipartFile file) throws UnirestException, IOException, InvalidFormatException {
	scbBilJourney.parseExcelForBilEtb(file);;
		return new ApiResponse(200,"Get the file from this location after some time","");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/bilScoringNtb")
	public ApiResponse testBilScoringNtb(@RequestParam("file") MultipartFile file) throws UnirestException, IOException, InvalidFormatException {
	scbBilJourney.parseExcelForBilNtb(file);
		return new ApiResponse(200,"Get the file from this location after some time","");
	}
}
