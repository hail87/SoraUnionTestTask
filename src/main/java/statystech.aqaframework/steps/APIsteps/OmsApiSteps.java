package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OmsDto.Response;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.utils.ApiRestUtils;

import java.io.IOException;

public class OmsApiSteps {

    private static final Logger logger = LoggerFactory.getLogger(OmsApiSteps.class);

    public String sendPostRequestAndSaveResponseToContext(String jsonFileName, TestInfo testInfo) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo.getTestMethod().get().getName(), LwaTestContext.class);
        String responseString = new ApiRestUtils().submitWebsiteOrder(jsonFileName);
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
}
