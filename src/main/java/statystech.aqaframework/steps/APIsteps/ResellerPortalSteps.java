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
}
