package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ResellerPortal.Reseller;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;

import java.io.IOException;

public class ResellerPortalSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(ResellerPortalSteps.class);

    public String getResellersList(int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().getResellersList(authToken);
        int statusCode = response.code();
        if (statusCode != expectedStatusCode) {
            logger.error(String.format("\nWrong response status code! Expected [%d], but found [%d]\nBody : [%s]",
                    expectedStatusCode,
                    statusCode,
                    response.body().string()));
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, statusCode);
        }

        String responseString = response.body().string();
        testContext.setResponseBody(responseString);
        logger.info("Response from API:\n" + responseString);
        if (responseString.contains("reseller_id")) {
            ObjectMapper mapper = new ObjectMapper();
            Reseller[] resellersListResponse = mapper.readValue(responseString, Reseller[].class);
            logger.info("\nResellers found: " + resellersListResponse.length);
            testContext.setResellersListResponse(resellersListResponse);
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String getResellerInformation(int resellerID, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().getResellerInformation(resellerID, authToken);
        int statusCode = response.code();
        if (statusCode != expectedStatusCode) {
            logger.error(String.format("\nWrong response status code! Expected [%d], but found [%d]\nBody : [%s]",
                    expectedStatusCode,
                    statusCode,
                    response.body().string()));
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, statusCode);
        }

        String responseString = response.body().string();
        testContext.setResponseBody(responseString);
        Context.updateTestContext(testContext);
        logger.info("Response from API:\n" + responseString);
//        if (responseString.contains("email")) {
//            ObjectMapper mapper = new ObjectMapper();
//            Reseller[] resellersListResponse = mapper.readValue(responseString, Reseller[].class);
//            logger.info("\nResellers found: " + resellersListResponse.length);
//            testContext.setResellersListResponse(resellersListResponse);
//            Context.updateTestContext(testContext);
//        }
        return "";
    }

    public String getWebsiteInformation(int websiteID, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().getWebsiteInformation(websiteID, authToken);
        int statusCode = response.code();
        if (statusCode != expectedStatusCode) {
            logger.error(String.format("\nWrong response status code! Expected [%d], but found [%d]\nBody : [%s]",
                    expectedStatusCode,
                    statusCode,
                    response.body().string()));
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, statusCode);
        }

        String responseString = response.body().string();
        testContext.setResponseBody(responseString);
        Context.updateTestContext(testContext);
        logger.info("Response from API:\n" + responseString);
//        if (responseString.contains("email")) {
//            ObjectMapper mapper = new ObjectMapper();
//            Reseller[] resellersListResponse = mapper.readValue(responseString, Reseller[].class);
//            logger.info("\nResellers found: " + resellersListResponse.length);
//            testContext.setResellersListResponse(resellersListResponse);
//            Context.updateTestContext(testContext);
//        }
        return "";
    }

    public String validateGetResellersInformationRequiredFields(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(verifyJsonResponseContainsAttribute("email", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("phone", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("account_balance", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("currency", lwaTestContext));
        return errorMessage.toString();
    }

    public String validateGetWebsitesInformationRequiredFields(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(verifyJsonResponseContainsAttribute("website_name", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("api_key", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("ip_range", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("region_id", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("region_name", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("statys_payment", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("max_discount_cap", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("refund_threshold", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("revenue", lwaTestContext));
        return errorMessage.toString();
    }
}
