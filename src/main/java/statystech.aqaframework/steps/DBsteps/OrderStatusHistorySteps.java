package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.OrderStatusHistoryTable;
import statystech.aqaframework.steps.Steps;

public class OrderStatusHistorySteps extends Steps {

    OrderStatusHistoryTable orderStatusHistoryTable = new OrderStatusHistoryTable();

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
        if (!orderStatusHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(orderId))){
            return String.format("\nRow with orderId '%s' wasn't found at the orderStatusHistory Table\n", orderId);
        }
        int actualOrderStatusID = orderStatusHistoryTable.getOrderStatusID(orderId);
        return verifyExpectedResults(actualOrderStatusID, expectedOrderStatusID);
    }
}
