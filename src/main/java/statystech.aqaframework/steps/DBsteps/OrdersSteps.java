package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DataUtils;

import java.sql.SQLException;

public class OrdersSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrdersSteps.class);

    public String checkOrdersTable() throws SQLException {
        OrdersTable ordersTable = new OrdersTable();
        StringBuilder errorMessage = new StringBuilder();
        int orderLineID = ordersTable.getPrimaryID();
        errorMessage.append(verifyExpectedResults(
                ordersTable.getJsonAndTableValue(orderLineID, "order_date")));
        errorMessage.append(checkOrderID());
        return errorMessage.toString();
    }

    public String checkOrderID() throws SQLException {
        String expectedOrderID = TestContext.JSON_OBJECT.get("order_id").toString();
        //Remove ["] symbol at the beginning and end of the String.
        expectedOrderID = expectedOrderID.substring(1,expectedOrderID.length() -1 );
        String actualOrderID = new OrdersTable().getOrderAllSysIDValue();
        if (actualOrderID.equalsIgnoreCase(expectedOrderID)) {
            logger.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() passed successfully\n");
            return "";
        } else {
            logger.error(new Object(){}.getClass().getEnclosingMethod().getName() + "() not passed\n");
            return "Wrong orders.orderAllSysID value found\nActual: '" +
                    actualOrderID + "'\nExpected: '" + expectedOrderID + "'";
        }
    }

//    private String checkComments() throws SQLException {
//        String actualComments = new OrdersTable().getColumnValue("comments");
//        String expectedComments = DataUtils.getValueFromJSON("shipping_notes");
//        return verifyExpectedResults(actualComments, expectedComments);
//    }
}
