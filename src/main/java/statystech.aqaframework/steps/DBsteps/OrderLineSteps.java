package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.TableObjects.OrderLineTable;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class OrderLineSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderLineSteps.class);

    public String checkProductIsAbsent(String productName) {
        try {
            checkName(productName);
            return String.format("Product with name '%s' have been found, but shouldn't", productName);
        } catch (SQLException throwables) {
            return "";
        }
    }

    public String checkOrderLineTableAndSetWarehouseOrderID(Product product) throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkName(product));
        errorMessage.append(checkSKU(product));
        errorMessage.append(checkPrice(product));
        errorMessage.append(checkQuantity(product));
        //addWarehouseOrderID(product);
        return errorMessage.toString();
    }

    public String checkOrderLineTableWithWarehouseOrderID(Product product) throws SQLException {
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

    private String checkName(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product.getProductName(), "productName");
        String expected = product.getProductName();
        return verifyExpectedResults(actual, expected);
    }

    private String checkName(String productName) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(productName, "productName");
        return verifyExpectedResults(actual, productName);
    }

    private String checkSKU(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product.getProductName(), "sku");
        String expected = product.getProductSKU();
        return verifyExpectedResults(actual, expected);
    }

    private String checkPrice(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product.getProductName(), "itemPrice");
        String expected = product.getProductItemPrice();
        return verifyExpectedResults(actual, expected);
    }

    private String checkQuantity(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product.getProductName(), "quantity");
        String expected = product.getProductQuantity();
        return verifyExpectedResults(actual, expected);
    }

    private String checkWarehouseOrderID(Product product) throws SQLException {
        if (TestContext.warehouseOrders != null) {
            String expected = String.valueOf(TestContext.warehouseOrders);
            String actual = new OrderLineTable().getColumnValueByProductName(product.getProductName(), "warehouseOrderID");
            return verifyExpectedResults(actual, expected);
        } else {
            logger.warn("There is no warehouseOrderID set at the TestContext yet");
            return "There is no warehouseOrderID set at the TestContext yet";
        }
    }

//    private void addWarehouseOrderID(Product product) throws SQLException {
//        int warehouseOrderID = Integer.parseInt(new OrderLineTable().getColumnValueByProductName(product, "warehouseOrderID"));
//        if(!TestContext.warehouseOrder.contains(warehouseOrderID))
//        TestContext.warehouseOrder.add(warehouseOrderID);
//    }
}
