package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ProductJson.CatalogManagement.GetVariantsResponse;
import statystech.aqaframework.DataObjects.ProductJson.CatalogManagement.ProductSearchResponse;
import statystech.aqaframework.DataObjects.ProductJson.CatalogManagement.ProductItem;
import statystech.aqaframework.DataObjects.ProductJson.CatalogManagement.Variant;
import statystech.aqaframework.DataObjects.ProductJson.Product;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;

import java.io.IOException;
import java.util.List;

public class CatalogManagementSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(CatalogManagementSteps.class);

    public String addProductParent(String productJson, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().addProductParent(productJson, authToken);
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
        if (responseString.contains("product_id")) {
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(responseString, Product.class);
            String productId = product.getProductId();
            logger.info("\nProductId: " + productId);
            testContext.setProductParentID((Integer.parseInt(productId)));
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String addProduct(int productParentId, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        okhttp3.Response response = new ApiRestUtils().addProduct(productParentId, authToken);
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
        if (responseString.contains("variant_id")) {
            int productId = Integer.parseInt(responseString.replaceAll("\\D", ""));
            logger.info("\nProductId: " + productId);
            testContext.setProductID(productId);
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String searchProduct(String productName, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        logger.info("\nSearching for a product : " + productName);
        okhttp3.Response response = new ApiRestUtils().searchProductCatalogManagement(productName, authToken);
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
        if (!responseString.contains("product_id")) {
            logger.info("\nNo products found: " + productName);
            testContext.setProducts(null);
            Context.updateTestContext(testContext);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            ProductSearchResponse productSearchResponse = mapper.readValue(responseString, ProductSearchResponse.class);
            List<ProductItem> products = productSearchResponse.getProducts();
            testContext.setProducts(products);
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String searchProductPartially(String productName, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        logger.info("\nSearching (partially) for a product : " + productName);
        okhttp3.Response response = new ApiRestUtils().partialSearchProductCatalogManagement(productName, authToken);
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
        if (!responseString.contains("product_id")) {
            logger.info("\nNo products found: " + productName);
            testContext.setProducts(null);
            Context.updateTestContext(testContext);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            ProductSearchResponse productSearchResponse = mapper.readValue(responseString, ProductSearchResponse.class);
            List<ProductItem> products = productSearchResponse.getProducts();
            testContext.setProducts(products);
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String getVariants(int productParentID, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        logger.info("\nSearching variants for productParent ID : " + productParentID);
        okhttp3.Response response = new ApiRestUtils().getVariantsCatalogManagement(productParentID, authToken);
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
        if (!responseString.contains("variant_id")) {
            logger.info("\nNo variants found for productParent ID : " + productParentID);
            testContext.setVariants(null);
            Context.updateTestContext(testContext);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            GetVariantsResponse getVariantsResponse = mapper.readValue(responseString, GetVariantsResponse.class);
            List<Variant> variants = getVariantsResponse.getVariants();
            testContext.setVariants(variants);
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String searchProductPartiallyExcludingID(String productName, int idToExclude, int expectedStatusCode, String authToken, LwaTestContext testContext) throws IOException {
        logger.info("\nSearching (partially) for a product : " + productName);
        okhttp3.Response response = new ApiRestUtils().partialSearchExcludingIDsProductCatalogManagement(productName, idToExclude, authToken);
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
        if (!responseString.contains("product_id")) {
            logger.info("\nNo products found: " + productName);
            testContext.setProducts(null);
            Context.updateTestContext(testContext);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            ProductSearchResponse productSearchResponse = mapper.readValue(responseString, ProductSearchResponse.class);
            List<ProductItem> products = productSearchResponse.getProducts();
            testContext.setProducts(products);
            Context.updateTestContext(testContext);
        }
        return "";
    }

    public String validateSearchResponseRequiredFields(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(verifyJsonResponseContainsAttribute("products.product_id", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("products.product_name", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("products.brand_name", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("products.manufacturer", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("products.is_licensed", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("products.is_cold", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("products.hs_code", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("products.cooling_details", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("products.variant_count", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("products.is_active", lwaTestContext));
        return errorMessage.toString();
    }

    public String validateGetVariantsResponseRequiredFields(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(verifyJsonResponseContainsAttribute("items.variant_id", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("items.variantName", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("items.sku", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("items.is_active", lwaTestContext));
        errorMessage.append(verifyJsonResponseContainsAttribute("items.is_brand", lwaTestContext));
        return errorMessage.toString();
    }
}
