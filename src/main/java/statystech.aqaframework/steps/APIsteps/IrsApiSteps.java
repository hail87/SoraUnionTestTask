package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.Response;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ParcelLines.AddProductButchResponse;
import statystech.aqaframework.DataObjects.ProductBatch.ProductBatchResponse;
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

    public String sendPostPartialProductSearchAndSaveResponseToContext(String partOfProductName, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().partialSearchProduct(partOfProductName, authToken);
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

    public String sendPostPartialProductSearchAndSaveResponseToContext(String partOfProductName, String excludedProductIds, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().partialSearchProduct(partOfProductName, excludedProductIds, authToken);
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

    public String verifyResultNotContain(String excludedIds, LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        for (int i = 0; i < lwaTestContext.getSearchProductResponse().getProductRecords().size(); i++) {
            if (lwaTestContext.getSearchProductResponse().getProductRecords().get(i).getProductId() == Integer.parseInt(excludedIds))
            errorMessage.append(String.format("Id '%d' was found at the result, but shouldn't", excludedIds));
        }
        return errorMessage.toString();
    }

    public String verifySearchResponseProductUnavailable(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(verifyJsonResponseContainsNotNullAttribute("record_count", 0, lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("product_records", lwaTestContext));
        return errorMessage.toString();
    }

    public String verifySearchResponseProductEmpty(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(verifyJsonResponseContainsNotNullAttribute("product_records", "[]", lwaTestContext));
        return errorMessage.toString();
    }

    @SneakyThrows
    public String sendPostAddNewProductBatchAndSaveResponseToContext( int productID, int warehouseId, int expectedStatusCode, String authToken, LwaTestContext testContext) {
        Response response = new ApiRestUtils().sendPostAddNewProductBatch(productID, warehouseId, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code() != expectedStatusCode) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, response.code());
        } else {
            String responseBody = response.body().string();
            testContext.setResponseBody(responseBody);
            ObjectMapper mapper = new ObjectMapper();
            ProductBatchResponse productButchResponse = mapper.readValue(responseBody, ProductBatchResponse.class);
            testContext.setProductBatchId(productButchResponse.getProductBatchId());
            Context.updateTestContext(testContext);
            return "";
        }
    }

    @SneakyThrows
    public String sendPostAddNewProductBatchAndSaveResponseToContext( int productID,
                                                                      int warehouseId,
                                                                      int expectedStatusCode,
                                                                      String authToken,
                                                                      String date,
                                                                      LwaTestContext testContext) {
        Response response = new ApiRestUtils().sendPostAddNewProductBatch(productID, warehouseId, date, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code() != expectedStatusCode) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, response.code());
        } else {
            String responseBody = response.body().string();
            testContext.setResponseBody(responseBody);
            ObjectMapper mapper = new ObjectMapper();
            ProductBatchResponse productButchResponse = mapper.readValue(responseBody, ProductBatchResponse.class);
            testContext.setProductBatchId(productButchResponse.getProductBatchId());
            Context.updateTestContext(testContext);
            return "";
        }
    }

//    public String addNewProductButch(String authToken, int productID, int freeStock, TestInfo testInfo) throws IOException {
//        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
//        Response response = new ApiRestUtils().sendPostParcelLine(productID, freeStock, authToken);
//        logger.info("Response from API:\n" + response.code());
//        if (response.code() != 200) {
//            return String.format("\nWrong response status code! Expected [%d], but found [%d]", 200, response.code());
//        } else {
//            ObjectMapper mapper = new ObjectMapper();
//            AddProductButchResponse addProductButchResponse = mapper.readValue(response.body().string(), AddProductButchResponse.class);
//            testContext.setProductBatchId(addProductButchResponse.getProductBatchId());
//            Context.updateTestContext(testContext);
//            return "";
//        }
//    }
}
