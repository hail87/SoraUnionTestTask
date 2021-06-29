package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.OrderExceptionHistoryTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

public class OrderExceptionHistorySteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderExceptionHistorySteps.class);

    OrderExceptionHistoryTable orderExceptionHistoryTable = new OrderExceptionHistoryTable();

    public String verifyRowWithOrderIdExist(LwaTestContext lwaTestContext) {
        boolean rowIsPresent = false;
        int i = 0;
        while(!orderExceptionHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(lwaTestContext.getApiOrderId())) && i < 3)
        {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
        rowIsPresent = orderExceptionHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(lwaTestContext.getApiOrderId()));

        if (!rowIsPresent) {
            return "There is no row with orderID : " + lwaTestContext.getApiOrderId();
        }
        return "";
    }
}
