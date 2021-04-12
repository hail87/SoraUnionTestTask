package statystech.aqaframework.steps.DBsteps;

import org.hamcrest.Condition;
import statystech.aqaframework.TableObjects.WarehouseOrderTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DataUtils;

import java.sql.SQLException;

public class WarehouseOrderSteps extends Steps {

    public String checkWarehouseOrderTable() throws SQLException {
        WarehouseOrderTable WarehouseOrderTable = new WarehouseOrderTable();
        StringBuilder errorMessage = new StringBuilder();
//        int orderLineID = WarehouseOrderTable.getPrimaryID();
//        errorMessage.append(verifyExpectedResults(
//                WarehouseOrderTable.getJsonAndTableValue(orderLineID, "order_items", "product_name")));
        errorMessage.append(checkComments());
        return errorMessage.toString();
    }

    private String checkComments() throws SQLException {
        String actualComments = new WarehouseOrderTable().getColumnValue("comments");
        String expectedComments = DataUtils.getValueFromJSON("shipping_notes");
        return verifyExpectedResults(actualComments, expectedComments);
    }

    private String checkQuantity() throws SQLException {
        String actual = new WarehouseOrderTable().getColumnValue("quantity");
        String expected = DataUtils.getValueFromJSON("order_items", "product_quantity");
        return verifyExpectedResults(actual, expected);
    }
}
