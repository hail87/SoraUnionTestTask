package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OmsDto.OmsSubmitOrderJson;
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
        if (responseString.isEmpty()) {
            return "\nResponse body is empty!\n";
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(responseString, Response.class);
            testContext.setApiOrderId(response.getOrderId());
            testContext.setApiBuyerAccountId(response.getBuyerAccountId());
            testContext.setApiOrderStatusCd(response.getOrderStatusCd());
            testContext.setResponse(response);
            return "";
        }
    }

    public String updateBuyerAccountIdAndSendPOST(TestInfo testInfo) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo.getTestMethod().get().getName(), LwaTestContext.class);
        testContext.updateBuyerAccountID();
        Context.updateTestContext(testContext);
        String jsonString = JsonUtils.serializeJsonObjectToJsonString(OmsSubmitOrderJson.class);
        String responseString = new ApiRestUtils().submitOrderAndGetString(jsonString);
        logger.info("Response from API:\n" + responseString);
        if (responseString.isEmpty()) {
            return "\nResponse body is empty!\n";
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(responseString, Response.class);
            testContext.setApiOrderId(response.getOrderId());
            testContext.setApiBuyerAccountId(response.getBuyerAccountId());
            testContext.setApiOrderStatusCd(response.getOrderStatusCd());
            testContext.setResponse(response);
            return "";
        }
    }

    public String sendPostRequestAndWaitForStatusCode(String jsonFileName, int code) {
        String errorMessage = "";
        int responseCode;
        responseCode = new ApiRestUtils().submitWebsiteOrder(jsonFileName).code();
        logger.info("ResponseCode:\n" + responseCode);
        if (responseCode!=code){
            errorMessage = String.format("Actual response code '%d' is different from expected one '%d'\n", responseCode, code);
        }
        return errorMessage;
    }
}
