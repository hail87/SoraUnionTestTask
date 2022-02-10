package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.DataUtils;
import statystech.aqaframework.utils.JsonUtils;
import statystech.aqaframework.DataObjects.IrsDto.SearchProductResponse;

import java.io.IOException;

public class IrsApiSteps {

    private static final Logger logger = LoggerFactory.getLogger(IrsApiSteps.class);

    public String sendPostProductSearchAndSaveResponseToContext(String productName, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().searchProduct(productName, authToken);

        if (response.code() != expectedStatusCode)
            return String.format("\n%s\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, response.code());

        String responseString = response.body().string();
        logger.info("Response from API:\n" + responseString);
        if (responseString.contains("product_name")) {
            ObjectMapper mapper = new ObjectMapper();
            SearchProductResponse searchProductResponse = mapper.readValue(responseString, SearchProductResponse.class);
            testContext.setSearchProductResponse(searchProductResponse);
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String verifySearchResponse(LwaTestContext testContext) throws IOException {
        testContext.getSearchProductResponse().getProductRecords().get(0).getProductName();
        return "";
    }
}
