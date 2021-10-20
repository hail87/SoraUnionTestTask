package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.WarehouseSearch.OrdersItem;
import statystech.aqaframework.DataObjects.WarehouseSearch.WarehouseSearchResponse;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

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
}
