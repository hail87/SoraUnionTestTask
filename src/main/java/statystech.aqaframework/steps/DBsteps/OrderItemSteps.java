package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.OrderItemTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;

public class OrderItemSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemSteps.class);

    OrderItemTable orderItemTable = new OrderItemTable();

    public String checkOrderID(LwaTestContext lwaTestContext) {
        int orderID = lwaTestContext.getApiOrderId();
        String jsonProductID = lwaTestContext.getJsonObject().get("line_items").getAsJsonArray().get(0).getAsJsonObject().get("product_id").toString();
        String dbProductID = DBUtils.executeAndReturnString(String.format("select productID from orderItem where orderID = '%d'", orderID));
        return verifyExpectedResults(jsonProductID, dbProductID);
    }
}
