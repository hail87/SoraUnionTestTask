package statystech.aqaframework.steps.APIsteps;

import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;

import java.io.IOException;

public class OrderLineApiSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderLineApiSteps.class);

    public String sendPutRequestAndSaveResponseToContext(int expectedStatusCode, int quantity, LwaTestContext testContext, String authToken) {
        Response response = new ApiRestUtils().sendPutOrderLine(testContext.getOrderLineID(), quantity, authToken);
        try {
            String body = response.body().string();
            testContext.setResponseBody(body);
            Context.updateTestContext(testContext);
            logger.info("Response body : " + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Response from API:\n" + response.code());
        if (response.code() != expectedStatusCode) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, response.code());
        } else {
            return "";
        }
    }

    public String sendPutRequestAndSaveResponseToContext(int expectedStatusCode, int quantity, int orderLineId, LwaTestContext testContext, String authToken) {
        Response response = new ApiRestUtils().sendPutOrderLine(orderLineId, quantity, authToken);
        try {
            String body = response.body().string();
            testContext.setResponseBody(body);
            Context.updateTestContext(testContext);
            logger.info("Response body : " + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Response from API:\n" + response.code());
        if (response.code() != expectedStatusCode) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, response.code());
        } else {
            return "";
        }
    }
}
