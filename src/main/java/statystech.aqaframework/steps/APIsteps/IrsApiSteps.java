package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.DataObjects.IrsDto.SearchProductResponse;

import java.io.IOException;

public class IrsApiSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(IrsApiSteps.class);

    public String sendGetProductDetailsAndSaveResponseToContext(int productId, int expectedStatusCode, String authToken,  LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().getProductDetails(productId, authToken);
        int statusCode = response.code();
        if ( statusCode!= expectedStatusCode) {
            logger.error(String.format("\nWrong response status code! Expected [%d], but found [%d]\nBody : [%s]",
                    expectedStatusCode,
                    statusCode,
                    response.body().string()));
            return String.format("\n%s\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, statusCode);
        }

        String responseString = response.body().string();
        testContext.setResponseBody(responseString);
        logger.info("Response from API:\n" + responseString);
        if (responseString.contains("product_name")) {
            ObjectMapper mapper = new ObjectMapper();
            SearchProductResponse searchProductResponse = mapper.readValue(responseString, SearchProductResponse.class);
            testContext.setSearchProductResponse(searchProductResponse);
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String sendPostProductSearchAndSaveResponseToContext(String productName, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().searchProduct(productName, authToken);
        int statusCode = response.code();
        if ( statusCode!= expectedStatusCode) {
            logger.error(String.format("\nWrong response status code! Expected [%d], but found [%d]\nBody : [%s]",
                    expectedStatusCode,
                    statusCode,
                    response.body().string()));
            return String.format("\n%s\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, statusCode);
        }

        String responseString = response.body().string();
        testContext.setResponseBody(responseString);
        logger.info("Response from API:\n" + responseString);
        if (responseString.contains("product_name")) {
            ObjectMapper mapper = new ObjectMapper();
            SearchProductResponse searchProductResponse = mapper.readValue(responseString, SearchProductResponse.class);
            testContext.setSearchProductResponse(searchProductResponse);
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String verifySearchResponse(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(verifyJsonResponseContainsAttribute("record_count", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.product_id", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.product_name", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.product_brand", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.product_manufacture", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.product_coo", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.is_license", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.item_price", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.item_currency", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.product_dosage_type", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.product_dosage_value", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.product_items_in_pack", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.product_pack_type", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.is_cold", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.total_available", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records.tiers", lwaTestContext));
        return errorMessage.toString();
    }

    public String verifyGetProductDetailsResponse(int productID, LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(verifyJsonResponseContainsNotNullAttribute("product_id", productID, lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_name", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_short_desc", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_dosage_value", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_dosage_type", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_items_in_pack", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_pack_type", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_sku", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("is_cold", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.warehouse_id", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.warehouse_name", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.threshold", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.total_available_product", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.batches.warehouse_batch_inventory_id", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.batches.product_batch_id", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.batches.batch_number", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.batches.exp_date", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.batches.available", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.batches.shipped", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("warehouses.batches.written_off", lwaTestContext));
        return errorMessage.toString();
    }

    public String verifyAllProductsAtTheSearchResponseContainsString(String partOfName, LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        for (int i = 0; i < lwaTestContext.getSearchProductResponse().getProductRecords().size(); i++) {
            errorMessage.append(verifyExpectedResultsContains(lwaTestContext.getSearchProductResponse().getProductRecords().get(i).getProductName(), partOfName));
        }
        return errorMessage.toString();
    }

    public String verifySearchResponseProductUnavailable(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(verifyJsonResponseContainsNotNullAttribute("record_count", 0, lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records", lwaTestContext));
        return errorMessage.toString();
    }
}
