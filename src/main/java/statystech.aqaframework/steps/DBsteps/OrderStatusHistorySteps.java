package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.OrderStatusHistoryTable;
import statystech.aqaframework.steps.Steps;

public class OrderStatusHistorySteps extends Steps {

    OrderStatusHistoryTable orderStatusHistoryTable = new OrderStatusHistoryTable();

    public OrderStatusHistorySteps(){
        super.tableObject = orderStatusHistoryTable;
    }
}
