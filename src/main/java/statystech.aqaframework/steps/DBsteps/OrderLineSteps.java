package statystech.aqaframework.steps.DBsteps;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OrderJackson.OrderItem;
import statystech.aqaframework.TableObjects.OrderLineTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DataUtils;

import java.sql.SQLException;

public class OrderLineSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderLineSteps.class);

    private OrderLineTable orderLineTable = new OrderLineTable();

    public String checkProductIsAbsent(String productName) {
        try {
            if (checkName(productName).isEmpty()) {
                return String.format("Product with name '%s' have been found, but shouldn't", productName);
            } else return "";
        } catch (SQLException throwables) {
            return "";
        }
    }

    public String checkOrderLineTableAndSetWarehouseOrderID(OrderItem product) throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkName(product));
        errorMessage.append(checkSKU(product));
        errorMessage.append(checkPrice(product));
        errorMessage.append(checkQuantity(product));
        addWarehouseOrderID(product);
        return errorMessage.toString();
    }

    public String checkOrderLineTableWithWarehouseOrderID(OrderItem product) throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        String isProductWithWarehouseOrderIDExist = checkWarehouseOrderID(product);
        if (isProductWithWarehouseOrderIDExist.isEmpty()) {
            errorMessage.append(checkName(product));
            errorMessage.append(checkSKU(product));
            errorMessage.append(checkPrice(product));
            errorMessage.append(checkQuantity(product));
            return errorMessage.toString();
        } else {
            return isProductWithWarehouseOrderIDExist;
        }
    }

    private String checkName(OrderItem product) throws SQLException {
        return checkName(product.getProductName());
    }

    private String checkName(String productName) throws SQLException {

        String actual = productName.contains("®") ?
                orderLineTable.getColumnValueContainsProductName(productName, "productName")
                : orderLineTable.getColumnValueByProductName(productName, "productName");
        return verifyExpectedResults(StringEscapeUtils.unescapeJava(actual), productName);
    }

    private String checkSKU(OrderItem product) throws SQLException {
        String actual = orderLineTable.getColumnValueByProductName(product.getProductName(), "sku");
        String expected = product.getSKU();
        return verifyExpectedResults(actual, expected);
    }

    private String checkPrice(OrderItem product) throws SQLException {
        String actual = orderLineTable.getColumnValueByProductName(product.getProductName(), "itemPrice");
        String expected = product.getProductItemPrice();
        return verifyExpectedResults(DataUtils.decrypt(actual), expected);
    }

    private String checkQuantity(OrderItem product) throws SQLException {
        String actual = orderLineTable.getColumnValueByProductName(product.getProductName(), "quantity");
        String expected = product.getProductQuantity();
        return verifyExpectedResults(DataUtils.decrypt(actual), expected);
    }

    private String checkWarehouseOrderID(OrderItem product) throws SQLException {
        if (Context.getTestContext(LwaTestContext.class).getWarehouseOrders() != null) {
            String expected = String.valueOf(Context.getTestContext(LwaTestContext.class).getLastWarehouseOrderID());
            String productName = product.getProductName();
            String actual = productName.contains("®") ?
                    orderLineTable.getColumnValueContainsProductName(productName, "warehouseOrderID")
                    : orderLineTable.getColumnValueByProductName(productName, "warehouseOrderID");
            return verifyExpectedResults(actual, expected);
        } else {
            logger.warn("There is no warehouseOrderID set at the TestContext yet");
            return "There is no warehouseOrderID set at the TestContext yet";
        }
    }

    private void addWarehouseOrderID(OrderItem product) throws SQLException {
        LwaTestContext lwaTestContext = Context.getTestContext(LwaTestContext.class);
        int warehouseOrderID = Integer.parseInt(orderLineTable.getColumnValueByProductName(product.getProductName(), "warehouseOrderID"));
        lwaTestContext.addWarehouseOrders(warehouseOrderID, new WarehouseOrderSteps().getWarehouseId(warehouseOrderID));
        Context.updateTestContext(lwaTestContext);
    }

    public void setOrderLineIDtoContext(int warehouseOrderId, LwaTestContext testContext){
        try {
            testContext.setOrderLineID(orderLineTable.getPrimaryID("warehouseOrderID", String.valueOf(warehouseOrderId)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Context.updateTestContext(testContext);
    }
}
