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

    public String checkProductIsAbsent(Product product) {
        try {
            checkName(product);
            return String.format("Product with name '%s' have been found, but shouldn't", product.getProductName());
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
        String actual = new OrderLineTable().getColumnValueByProductName(product, "productName");
        String expected = product.getProductName();
        return verifyExpectedResults(actual, expected);
    }

    private String checkSKU(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product, "sku");
        String expected = product.getProductSKU();
        return verifyExpectedResults(actual, expected);
    }

    private String checkPrice(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product, "itemPrice");
        String expected = product.getProductItemPrice();
        return verifyExpectedResults(actual, expected);
    }

    private String checkQuantity(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValueByProductName(product, "quantity");
        String expected = product.getProductQuantity();
        return verifyExpectedResults(actual, expected);
    }

    private String checkWarehouseOrderID(Product product) throws SQLException {
        if (TestContext.warehouseOrders.get(0) != 0) {
            String expected = String.valueOf(TestContext.warehouseOrders);
            String actual = new OrderLineTable().getColumnValueByProductName(product, "warehouseOrderID");
            return verifyExpectedResults(actual, expected);
        } else {
            return "There is no warehouseOrderID set at the TestContext yet";
        }
    }

//    private void addWarehouseOrderID(Product product) throws SQLException {
//        int warehouseOrderID = Integer.parseInt(new OrderLineTable().getColumnValueByProductName(product, "warehouseOrderID"));
//        if(!TestContext.warehouseOrder.contains(warehouseOrderID))
//        TestContext.warehouseOrder.add(warehouseOrderID);
//    }
}
