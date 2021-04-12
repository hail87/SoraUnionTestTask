package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.common.TestContext;

import java.sql.SQLException;

public class OrdersSteps {

    private static final Logger logger = LoggerFactory.getLogger(OrdersSteps.class);

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
}
