package statystech.aqaframework.steps.DBsteps;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;

public class OrdersSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrdersSteps.class);

    OrdersTable ordersTable = new OrdersTable();

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

    public String checkApiResponse(LwaTestContext lwaTestContext) {
        return checkBuyerAccountId(lwaTestContext.getApiOrderId(), lwaTestContext.getApiBuyerAccountId());
    }

    public void setOMSShippingAddressIDToContext(LwaTestContext lwaTestContext) {
        int omsShippingAddressID = ordersTable.getOMSShippingAddressID(lwaTestContext.getApiOrderId());
        lwaTestContext.setOmsShippingAddressID(omsShippingAddressID);
        logger.info("\nomsShippingAddressID: " + omsShippingAddressID);
        Context.updateTestContext(lwaTestContext);
    }

    public int getOMSShippingAddressID(int orderID) {
        return ordersTable.getOMSShippingAddressID(orderID);
    }

    public int getOMSBillingAddressID(int orderID) {
        return ordersTable.getOMSBillingAddressID(orderID);
    }

    public String checkOMSShippingAddressID(int primaryID, int expectedOMSShippingAddressID) {
        int omsShippingAddressID = ordersTable.getOMSShippingAddressID(primaryID);
        logger.info("\nomsShippingAddressID: " + omsShippingAddressID);
        return verifyExpectedResults(omsShippingAddressID, expectedOMSShippingAddressID);
    }

    public void setOMSBillingAddressIDToContext(LwaTestContext lwaTestContext) {
        lwaTestContext.setOmsBillingAddressID(ordersTable.getOMSBillingAddressID(lwaTestContext.getApiOrderId()));
        logger.info("\nomsBillingAddressID: " + ordersTable.getOMSBillingAddressID(lwaTestContext.getApiOrderId()));
        Context.updateTestContext(lwaTestContext);
    }

    public void setOMSBuyerAccountLicenseIDToContext(LwaTestContext lwaTestContext) {
        lwaTestContext.setOMSBuyerAccountLicenseID(ordersTable.getOMSBuyerAccountLicenseID(lwaTestContext.getApiOrderId()));
        Context.updateTestContext(lwaTestContext);
    }

    private String checkBuyerAccountId(int orderId, int buyerAccountID) {
        try {
            int dbBuyerAccountID = ordersTable.getBuyerAccountId(orderId);
            if (buyerAccountID == dbBuyerAccountID) {
                return "";
            } else {
                return String.format("buyerAccountID '%d' is different from dbBuyerAccountID '%d'", buyerAccountID, dbBuyerAccountID);
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return "There is no line with orderID: '" + orderId + "'";
        }
    }

    private String checkOrder(int orderId) {
        try {
            ordersTable.getPrimaryID("orderID", String.valueOf(orderId));
            return "";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no line with orderID: '" + orderId + "'";
        }
    }

    public String checkOrderDate() throws SQLException, IOException {
        int orderLineID = ordersTable.getPrimaryID();
        return verifyExpectedResults(ordersTable.getJsonAndTableValue(orderLineID, "order_date"));
    }

    public String checkOrderIsCancelled() throws SQLException, JsonProcessingException {
        return checkOrderStatusName("Cancelled");
    }

    private String checkOrderStatusName(String expectedStatus) throws SQLException, JsonProcessingException {
        String actual = ordersTable.getOrderStatusName();
        return verifyExpectedResults(actual, expectedStatus);
    }

    private String checkOrderStatusID(String expected) throws SQLException, JsonProcessingException {
        String actual = ordersTable.getOrderStatusID();
        return verifyExpectedResults(actual, expected);
    }

    public String checkOrderAllSysID() {
        String expectedOrderID = Context.getTestContext(LwaTestContext.class).getJsonObject().get("order_id").toString();
        expectedOrderID = expectedOrderID.substring(1, expectedOrderID.length() - 1);
        String actualOrderID = new OrdersTable().getOrderAllSysIDValue();
        if (actualOrderID.equalsIgnoreCase(expectedOrderID)) {
            logger.info(new Object() {
            }.getClass().getEnclosingMethod().getName() + "() passed successfully\n");
            Context.getTestContext(LwaTestContext.class).setOrderAllSysID(actualOrderID);
            return "";
        } else {
            logger.error(new Object() {
            }.getClass().getEnclosingMethod().getName() + "() not passed\n");
            return "Wrong orders.orderAllSysID value found\nActual: '" +
                    actualOrderID + "'\nExpected: '" + expectedOrderID + "'";
        }
    }

    private String checkCurrency() throws SQLException {
        String actual = ordersTable.getCurrencyValue();
        String expected = JsonUtils.getValueFromJSON("order_currency");
        return verifyExpectedResults(actual, expected);
    }

    private String checkCurrencyConversion() throws SQLException {
        String actual = ordersTable.getCurrencyConversionValue();
        Double expected = Double.parseDouble(JsonUtils.getValueFromJSON("currency_conversion"));
        return verifyExpectedResults(actual, expected.toString());
    }

    public void setOrderID() throws SQLException, IOException {
        LwaTestContext lwaTestContext = Context.getTestContext(LwaTestContext.class);
        lwaTestContext.setOrderID(ordersTable.getPrimaryID());
        Context.updateTestContext(lwaTestContext);
    }

    public void setPaymentMethodID() {
        LwaTestContext lwaTestContext = Context.getTestContext(LwaTestContext.class);
        lwaTestContext.setPaymentMethodID(ordersTable.getPaymentMethodID(lwaTestContext.getApiOrderId()));
        Context.updateTestContext(lwaTestContext);
    }

    public String verifyOrderStatusName(int orderID, String expectedStatus) {
        String actualStatus = DBUtils.executeAndReturnString(
                String.format("select orderStatusName from %s where orderID = %d", ordersTable.getName(), orderID));
        int i = 0;
        while(actualStatus.equalsIgnoreCase("New Order") && i < 3)
        {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            actualStatus = DBUtils.executeAndReturnString(
                    String.format("select orderStatusName from %s where orderID = %d", ordersTable.getName(), orderID));
            i++;
        }
        return verifyExpectedResults(actualStatus, expectedStatus);
    }

}
