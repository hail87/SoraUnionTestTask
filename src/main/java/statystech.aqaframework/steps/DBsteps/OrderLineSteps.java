package statystech.aqaframework.steps.DBsteps;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.Jackson.OrderItem;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.TableObjects.OrderLineTable;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrderLineSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderLineSteps.class);

    public String checkProductIsAbsent(String productName) {
        try {
            if(checkName(productName).isEmpty()) {
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
        String actual = new OrderLineTable().getColumnValueByProductName(StringEscapeUtils.unescapeJava(product.getProductName()), "productName");
        String expected = product.getProductName();
        return verifyExpectedResults(actual, expected);
    }

    private String checkName(String productName) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(productName, "productName");
        return verifyExpectedResults(actual, productName);
    }

    private String checkSKU(OrderItem product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product.getProductName(), "sku");
        String expected = product.getSKU();
        return verifyExpectedResults(actual, expected);
    }

    private String checkPrice(OrderItem product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product.getProductName(), "itemPrice");
        String expected = product.getProductItemPrice();
        return verifyExpectedResults(actual, expected);
    }

    private String checkQuantity(OrderItem product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product.getProductName(), "quantity");
        String expected = product.getProductQuantity();
        return verifyExpectedResults(actual, expected);
    }

    private String checkWarehouseOrderID(OrderItem product) throws SQLException {
        if (Context.getTestContext().getWarehouseOrders() != null) {
            String expected = String.valueOf(Context.getTestContext().getLastWarehouseOrderID());
            String actual = new OrderLineTable().getColumnValueByProductName(StringEscapeUtils.unescapeJava(product.getProductName()), "warehouseOrderID");
            return verifyExpectedResults(actual, expected);
        } else {
            logger.warn("There is no warehouseOrderID set at the TestContext yet");
            return "There is no warehouseOrderID set at the TestContext yet";
        }
    }

    private void addWarehouseOrderID(OrderItem product) throws SQLException {
        TestContext testContext = Context.getTestContext();
        int warehouseOrderID = Integer.parseInt(new OrderLineTable().getColumnValueByProductName(product.getProductName(), "warehouseOrderID"));
        testContext.addWarehouseOrders(warehouseOrderID, new WarehouseOrderSteps().getWarehouseId(warehouseOrderID));
        Context.updateTestContext(testContext);
    }
}
