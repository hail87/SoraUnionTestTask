package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OmsDto.Response;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;

public class OmsApiSteps {

    private static final Logger logger = LoggerFactory.getLogger(OmsApiSteps.class);

    public String sendPostRequestAndSaveResponseToContext(String jsonFileName, TestInfo testInfo) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo.getTestMethod().get().getName(), LwaTestContext.class);
        testContext.setJsonObject(JsonUtils.getJsonObject(jsonFileName));
        testContext.setSubmitOrderObjectsFromJson();
        String responseString = new ApiRestUtils().submitWebsiteOrderAndGetString(jsonFileName);
        logger.info("Response from API:\n" + responseString);
        Context.updateTestContext(testContext);
        if (!responseString.contains("order_id")) {
            return String.format("\nWrong response!\nResponseString:\n'%s'\n", responseString);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(responseString, Response.class);
            testContext.setApiOrderId(response.getOrderId());
            testContext.setApiBuyerAccountId(response.getBuyerAccountId());
            testContext.setApiOrderStatusCd(response.getOrderStatusCd());
            testContext.setResponse(response);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPostRequestWithWrongApiKeyAndSaveResponseToContext(String jsonFileName, TestInfo testInfo) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo.getTestMethod().get().getName(), LwaTestContext.class);
        testContext.setJsonObject(JsonUtils.getJsonObject(jsonFileName));
        testContext.setSubmitOrderObjectsFromJson();
        String responseString = new ApiRestUtils().submitWebsiteOrderAndGetString(jsonFileName, "Z6XqmojdLV3XigTe1ZHQJ9WMJwI4qeHR8oUrgzBG");
        logger.info("Response from API:\n" + responseString);
        Context.updateTestContext(testContext);
        if (!responseString.contains("order_id")) {
            return "\nWrong response!\n responseString: \n";
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(responseString, Response.class);
            testContext.setApiOrderId(response.getOrderId());
            testContext.setApiBuyerAccountId(response.getBuyerAccountId());
            testContext.setApiOrderStatusCd(response.getOrderStatusCd());
            testContext.setResponse(response);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPostRequestAndSaveResponseToContext(LwaTestContext testContext) throws IOException {
        String responseString = new ApiRestUtils().submitOrderAndGetString(testContext.getJsonString());
        logger.info("Response from API:\n" + responseString);
        if (!responseString.contains("order_id")) {
            return "\nWrong response!\n responseString: \n";
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(responseString, Response.class);
            testContext.setApiOrderId(response.getOrderId());
            testContext.setApiBuyerAccountId(response.getBuyerAccountId());
            testContext.setApiOrderStatusCd(response.getOrderStatusCd());
            testContext.setResponse(response);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public void updateBuyerAccountId(LwaTestContext testContext) {
        testContext.updateBuyerAccountID();
        String jsonString = JsonUtils.serializeJsonObjectToJsonString(testContext.getOmsSubmitOrderJson());
        testContext.setJsonString(jsonString);
        Context.updateTestContext(testContext);
    }

    public void updateBuyerAccountIp(LwaTestContext testContext, String ip) {
        testContext.updateBuyerIpAddress(ip);
        String jsonString = JsonUtils.serializeJsonObjectToJsonString(testContext.getOmsSubmitOrderJson());
        testContext.setJsonString(jsonString);
        Context.updateTestContext(testContext);
    }

    public String updateBuyerAccountIdAndSendPOST(TestInfo testInfo) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo.getTestMethod().get().getName(), LwaTestContext.class);
        testContext.updateBuyerAccountID();
        Context.updateTestContext(testContext);
        String jsonString = JsonUtils.serializeJsonObjectToJsonString(testContext.getOmsSubmitOrderJson());
        String responseString = new ApiRestUtils().submitOrderAndGetString(jsonString);
        logger.info("Response from API:\n" + responseString);
        if (!responseString.contains("order_id")) {
            return "\nWrong response!\n responseString: \n";
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(responseString, Response.class);
            testContext.setApiOrderId(response.getOrderId());
            testContext.setApiBuyerAccountId(response.getBuyerAccountId());
            testContext.setApiOrderStatusCd(response.getOrderStatusCd());
            testContext.setResponse(response);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPostRequestAndWaitForStatusCode(String jsonFileName, int statusCode) {
        String errorMessage = "";
        int responseCode;
        responseCode = new ApiRestUtils().submitWebsiteOrder(jsonFileName).code();
        logger.info("ResponseCode:\n" + responseCode);
        if (responseCode != statusCode) {
            errorMessage = String.format("Actual response code '%d' is different from expected one '%d'\n", responseCode, statusCode);
        }
        return errorMessage;
    }

    public String sendPostRequestWithFakeApiKeyAndWaitForStatusCode(String jsonFileName, int statusCode) {
        String errorMessage = "";
        int responseCode;
        responseCode = new ApiRestUtils().submitWebsiteOrder(jsonFileName, "rfyA0vcW2aQZHJBFXlKI4HUDuDeBJctxfBBaTW61").code();
        logger.info("ResponseCode:\n" + responseCode);
        if (responseCode != statusCode) {
            errorMessage = String.format("Actual response code '%d' is different from expected one '%d'\n", responseCode, statusCode);
        }
        return errorMessage;
    }
}
