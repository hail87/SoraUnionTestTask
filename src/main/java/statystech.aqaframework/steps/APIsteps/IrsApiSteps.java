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

    public String sendPostProductSearchAndSaveResponseToContext(String productName, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().searchProduct(productName, authToken);

        if (response.code() != expectedStatusCode)
            return String.format("\n%s\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, response.code());

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
