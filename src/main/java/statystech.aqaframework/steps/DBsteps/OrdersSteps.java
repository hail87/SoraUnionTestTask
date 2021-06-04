package statystech.aqaframework.steps.DBsteps;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;

public class OrdersSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrdersSteps.class);

    public OrdersSteps() throws SQLException, IOException {
        setOrderID();
    }

    public String checkOrdersTable() throws SQLException, IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkOrderDate());
        errorMessage.append(checkOrderAllSysID());
        errorMessage.append(checkCurrency());
        errorMessage.append(checkCurrencyConversion());
        errorMessage.append(checkOrderStatusID("1"));
        errorMessage.append(checkOrderStatusName("Confirmed"));
        return errorMessage.toString();
    }

    public String checkOrderDate() throws SQLException, IOException {
        OrdersTable ordersTable = new OrdersTable();
        int orderLineID = ordersTable.getPrimaryID();
        return verifyExpectedResults(ordersTable.getJsonAndTableValue(orderLineID, "order_date"));
    }

    public String checkOrderIsCancelled() throws SQLException, JsonProcessingException {
        return checkOrderStatusName("Cancelled");
    }

    private String checkOrderStatusName(String expectedStatus) throws SQLException, JsonProcessingException {
        String actual = new OrdersTable().getOrderStatusName();
        return verifyExpectedResults(actual, expectedStatus);
    }

    private String checkOrderStatusID(String expected) throws SQLException, JsonProcessingException {
        String actual = new OrdersTable().getOrderStatusID();
        return verifyExpectedResults(actual, expected);
    }

    public String checkOrderAllSysID() {
        String expectedOrderID = Context.getTestContext(LwaTestContext.class).getJsonObject().get("order_id").toString();
        expectedOrderID = expectedOrderID.substring(1,expectedOrderID.length() -1 );
        String actualOrderID = new OrdersTable().getOrderAllSysIDValue();
        if (actualOrderID.equalsIgnoreCase(expectedOrderID)) {
            logger.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() passed successfully\n");
            Context.getTestContext(LwaTestContext.class).setOrderAllSysID(actualOrderID);
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

    public void setOrderID() throws SQLException, IOException {
        LwaTestContext lwaTestContext = Context.getTestContext(LwaTestContext.class);
        lwaTestContext.setOrderID(new OrdersTable().getPrimaryID());
        Context.updateTestContext(lwaTestContext);
    }
}
