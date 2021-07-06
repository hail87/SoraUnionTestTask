package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.OrderStatusHistoryTable;
import statystech.aqaframework.steps.Steps;

public class OrderStatusHistorySteps extends Steps {

    OrderStatusHistoryTable orderStatusHistoryTable = new OrderStatusHistoryTable();

    public OrderStatusHistorySteps(){
        super.tableObject = orderStatusHistoryTable;
    }

    public String verifyRowWithOrderId (int orderId){
        if (!orderStatusHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(orderId))){
            return String.format("Row with orderId '%s' wasn't found at the orderStatusHistory Table", orderId);
        }
        return "";
    }
}
