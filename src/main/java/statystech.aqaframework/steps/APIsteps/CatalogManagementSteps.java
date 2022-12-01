package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ProductJson.Product;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;

import java.io.IOException;

public class CatalogManagementSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(CatalogManagementSteps.class);

    public String addProduct(String productJson, int expectedStatusCode, String authToken,  LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().addProduct(productJson, authToken);
        int statusCode = response.code();
        if ( statusCode!= expectedStatusCode) {
            logger.error(String.format("\nWrong response status code! Expected [%d], but found [%d]\nBody : [%s]",
                    expectedStatusCode,
                    statusCode,
                    response.body().string()));
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, statusCode);
        }

        String responseString = response.body().string();
        testContext.setResponseBody(responseString);
        logger.info("Response from API:\n" + responseString);
        if (responseString.contains("product_id")) {
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(responseString, Product.class);
            String productId = product.getProductId();
            logger.info("\nProductId: " + productId);
            testContext.setProductID(Integer.parseInt(productId));
            Context.updateTestContext(testContext);
        }
        return "";
    }
}
