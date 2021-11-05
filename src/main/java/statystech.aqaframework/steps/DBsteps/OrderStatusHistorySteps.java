package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.OrderStatusHistoryTable;
import statystech.aqaframework.steps.Steps;

public class OrderStatusHistorySteps extends Steps {

    OrderStatusHistoryTable orderStatusHistoryTable = new OrderStatusHistoryTable();

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusHistorySteps.class);

    public OrderStatusHistorySteps(){
        super.tableObject = orderStatusHistoryTable;
    }

    public String checkRowWithOrderIdIsPresent(int orderId){
        if (!orderStatusHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(orderId))){
            return String.format("Row with orderId '%s' wasn't found at the orderStatusHistory Table", orderId);
        }
        return "";
    }

    public String validateRowWithOrderIdHasOrderStatusID(int orderId, int expectedOrderStatusID){
        int i = 0;
        while (!orderStatusHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(orderId)) && i < 10) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.warn(String.format("OrderStatusHistorySteps:validateRowWithOrderIdHasOrderStatusID - Trying to find row with orderID '%d', attempt %d", orderId, i));
            }
            i++;
        }

        if (!orderStatusHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(orderId))) {
            return String.format("\nRow with orderId '%s' wasn't found at the orderStatusHistory Table\n", orderId);
        }

        int actualOrderStatusID = orderStatusHistoryTable.getOrderStatusID(orderId);
        return verifyExpectedResults(actualOrderStatusID, expectedOrderStatusID);
    }
}
