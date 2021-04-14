package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.OrderLineTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.JsonUtils;

import java.sql.SQLException;

public class OrderLineSteps extends Steps {

    public String checkOrderLineTable() throws SQLException {
        OrderLineTable orderLineTable = new OrderLineTable();
        StringBuilder errorMessage = new StringBuilder();
        int orderLineID = orderLineTable.getPrimaryID("productName", new ProductSteps().getProductDetailsValueFromJSON("product_name"));
        errorMessage.append(verifyExpectedResults(
                orderLineTable.getJsonAndTableValue(orderLineID, "order_items", "product_name")));
        errorMessage.append(verifyExpectedResults(
                orderLineTable.getJsonAndTableValue(orderLineID, "order_items", "SKU")));
        errorMessage.append(checkPrice());
        errorMessage.append(checkQuantity());
        return errorMessage.toString();
    }

    private String checkPrice() throws SQLException {
        String actualPhoneNumber = new OrderLineTable().getColumnValue("itemPrice");
        String expectedPhoneNumber = JsonUtils.getValueFromJSON("order_items", "product_item_price");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }

    private String checkQuantity() throws SQLException {
        String actualPhoneNumber = new OrderLineTable().getColumnValue("quantity");
        String expectedPhoneNumber = JsonUtils.getValueFromJSON("order_items", "product_quantity");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }
}
