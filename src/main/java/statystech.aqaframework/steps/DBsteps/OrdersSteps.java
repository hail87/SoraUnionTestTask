package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.JsonUtils;

import java.sql.SQLException;

public class OrdersSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrdersSteps.class);

    public OrdersSteps() throws SQLException {
        setOrderID();
    }

    public String checkOrdersTable() throws SQLException {
        OrdersTable ordersTable = new OrdersTable();
        StringBuilder errorMessage = new StringBuilder();
        int orderLineID = ordersTable.getPrimaryID();
        errorMessage.append(verifyExpectedResults(
                ordersTable.getJsonAndTableValue(orderLineID, "order_date")));
        errorMessage.append(checkOrderAllSysID());
        errorMessage.append(checkCurrency());
        errorMessage.append(checkCurrencyConversion());
        errorMessage.append(checkOrderStatusID("1"));
        errorMessage.append(checkOrderStatusName("Confirmed"));
        return errorMessage.toString();
    }

    public String checkOrderIsCancelled() throws SQLException {
        return checkOrderStatusName("Cancelled");
    }

    private String checkOrderStatusName(String expectedStatus) throws SQLException {
        String actual = new OrdersTable().getOrderStatusName();
        return verifyExpectedResults(actual, expectedStatus);
    }

    private String checkOrderStatusID(String expected) throws SQLException {
        String actual = new OrdersTable().getOrderStatusID();
        return verifyExpectedResults(actual, expected);
    }

    public String checkOrderAllSysID() {
        String expectedOrderID = TestContext.JSON_OBJECT.get("order_id").toString();
        expectedOrderID = expectedOrderID.substring(1,expectedOrderID.length() -1 );
        String actualOrderID = new OrdersTable().getOrderAllSysIDValue();
        if (actualOrderID.equalsIgnoreCase(expectedOrderID)) {
            logger.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() passed successfully\n");
            TestContext.orderAllSysID = actualOrderID;
            return "";
        } else {
            logger.error(new Object(){}.getClass().getEnclosingMethod().getName() + "() not passed\n");
            return "Wrong orders.orderAllSysID value found\nActual: '" +
                    actualOrderID + "'\nExpected: '" + expectedOrderID + "'";
        }
    }

    private String checkCurrency() throws SQLException {
        String actual = new OrdersTable().getCurrencyValue();
        String expected = JsonUtils.getValueFromJSON("order_currency");
        return verifyExpectedResults(actual, expected);
    }

    private String checkCurrencyConversion() throws SQLException {
        String actual = new OrdersTable().getCurrencyConversionValue();
        Double expected = Double.parseDouble(JsonUtils.getValueFromJSON("currency_conversion"));
        return verifyExpectedResults(actual, expected.toString());
    }

    public void setOrderID() throws SQLException {
        TestContext.orderID = new OrdersTable().getPrimaryID();
    }
}
