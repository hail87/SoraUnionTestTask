package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ProductBatch.ProductBatchResponse;
import statystech.aqaframework.DataObjects.WarehouseSearch.OrdersItem;
import statystech.aqaframework.DataObjects.WarehouseSearch.WarehouseSearchResponse;
import statystech.aqaframework.DataObjects.WebsiteSearch.GetWebSitesResponse;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LwaApiSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(LwaApiSteps.class);

    public String updateLwaContextWithWarehouseSearchResult(String jsonString, LwaTestContext testContext) throws IOException {
        logger.info("Response from API:\n" + jsonString);
        if (!jsonString.contains("allsys_order_id")) {
            return String.format("\nWrong response!\nResponseString:\n'%s'\n", jsonString);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            WarehouseSearchResponse response = mapper.readValue(jsonString, WarehouseSearchResponse.class);
            testContext.setWarehouseSearchResponse(response);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String checkWarehouseSearchResponse(ArrayList<Integer> expectedOrderNumbersList, LwaTestContext testContext){
        logger.info(String.format("\nExpected orders: %s", expectedOrderNumbersList));

        List<OrdersItem> actualOrdersList = testContext.getWarehouseSearchResponse().getOrders();
        List<Integer> actualOrderNumbersList = actualOrdersList.stream().map(OrdersItem::getAllsysOrderId).collect(Collectors.toList());
        logger.info(String.format("\nActual orders: %s", actualOrderNumbersList));

        if (!actualOrderNumbersList.containsAll(expectedOrderNumbersList)){
            ArrayList<Integer> delta = new ArrayList<>(expectedOrderNumbersList);
            delta.removeAll(actualOrderNumbersList);
            return String.format("\nExpected orders at the search response : '%s', found : '%s', orders missed : '%s'",
                    expectedOrderNumbersList, actualOrderNumbersList, delta);
        }
        return "";
    }

    @SneakyThrows
    public String sendGetWebsitesRequest(int expectedStatusCode, String authToken, LwaTestContext testContext) {
        Response response = new ApiRestUtils().sendGetWebsites(authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code() != expectedStatusCode) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, response.code());
        } else {
            String responseBody = response.body().string();
            testContext.setResponseBody(responseBody);
//            ObjectMapper mapper = new ObjectMapper();
//            GetWebSitesResponse getWebSitesResponse = mapper.readValue(responseBody, GetWebSitesResponse.class);
//            testContext.setGetWebsitesResponse(getWebSitesResponse);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String checkWebsitesResponseStructure(LwaTestContext testContext) {
        return verifyJsonResponseContainsNotNullAttribute("websites.website_id", testContext) +
                verifyJsonResponseContainsNotNullAttribute("websites.website_name", testContext) +
                verifyJsonResponseContainsNotNullAttribute("websites.website_currency", testContext);
    }

    public String checkWebsitesResponse(int userID, LwaTestContext testContext) {
        ArrayList<String> warehouseNumbers = DBUtils.executeAndReturnStringArray("SELECT websiteID FROM lwa_sandbox.websiteTeam where userID = " + userID);
        for (String warehouseNumber : warehouseNumbers){
            if (!testContext.getResponseBody().contains("\"website_id\":" + warehouseNumber))
                    return "There is no \n'\"website_id\": warehouseNumber'\n line found and the json response";
        }
        return "";
    }
}
