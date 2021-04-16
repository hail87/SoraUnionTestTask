package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.TableObjects.OrderLineTable;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class OrderLineSteps extends Steps {

    public String checkOrderLineTable(Product product) throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkName(product));
        errorMessage.append(checkSKU(product));
        errorMessage.append(checkPrice(product));
        errorMessage.append(checkQuantity(product));
        return errorMessage.toString();
    }

    private String checkName(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValue(product, "productName");
        String expected = product.getProductName();
        return verifyExpectedResults(actual, expected);
    }

    private String checkSKU(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValue(product, "sku");
        String expected = product.getProductSKU();
        return verifyExpectedResults(actual, expected);
    }

    private String checkPrice(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValue(product, "itemPrice");
        String expected = product.getProductItemPrice();
        return verifyExpectedResults(actual, expected);
    }

    private String checkQuantity(Product product) throws SQLException {
        String actual = new OrderLineTable().getColumnValue(product,"quantity");
        String expected = product.getProductQuantity();
        return verifyExpectedResults(actual, expected);
    }
}
