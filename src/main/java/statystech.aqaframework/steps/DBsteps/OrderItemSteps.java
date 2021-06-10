package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OmsDto.LineItemsItem;
import statystech.aqaframework.TableObjects.OrderItemTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

public class OrderItemSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemSteps.class);

    OrderItemTable orderItemTable = new OrderItemTable();

    public String checkProductIdsWithOrderID(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        for(LineItemsItem lineItemsItem : lwaTestContext.getOmsSubmitOrderJson().getLineItems()) {
            errorMessage.append(orderItemTable.checkProductIdWithOrderID(lwaTestContext.getApiOrderId(), lineItemsItem.getProductId()));
        }
        return errorMessage.toString();
    }
}
