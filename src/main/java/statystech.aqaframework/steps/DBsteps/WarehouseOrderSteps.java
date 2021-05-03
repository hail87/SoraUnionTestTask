package statystech.aqaframework.steps.DBsteps;


import lombok.SneakyThrows;
import org.apache.commons.lang3.StringEscapeUtils;
import statystech.aqaframework.TableObjects.WarehouseOrderTable;
import statystech.aqaframework.TableObjects.WarehouseTable;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WarehouseOrderSteps extends Steps {

    @SneakyThrows
    public WarehouseOrderSteps() {
        setWarehouseOrder();
    }

    public String checkWarehouseOrderTable() {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkWarehouseOrderStatusesIsActive());
        errorMessage.append(checkComments());
        return errorMessage.toString();
    }

    public String checkWarehouseOrderQuantity(int warehouseOrderQuantity) {
        StringBuilder errorMessage = new StringBuilder();
        if (TestContext.warehouseOrders.size() != warehouseOrderQuantity) {
            errorMessage.append(String.format(
                    "There is no %d but %d warehouseOrders detected", warehouseOrderQuantity, TestContext.warehouseOrders.size()));
        }
        return errorMessage.toString();
    }

    public String checkWarehouseOrderStatusesIsActive() {
        StringBuilder errorMessage = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : TestContext.warehouseOrders.entrySet()) {
            if (!new WarehouseOrderTable().checkWarehouseOrderStatus(entry.getKey()))
                errorMessage.append(String.format(
                        "warehouse order's status with ID %d isn't '1', what means that warehouseOrder is not active", entry.getKey()));
        }
        return errorMessage.toString();
    }

    public String checkWarehouseOrderIsNotActive(String warehouseName) throws SQLException {
        int wareHouseId = new WarehouseTable().getWarehouseId(warehouseName);
        int warehouseOrderID = TestContext.warehouseOrders.entrySet().stream().filter(p -> p.getValue().equals(wareHouseId)).findFirst().orElse(null).getKey();
        return checkWarehouseOrderIsNotActive(warehouseOrderID);
    }

    private String checkWarehouseOrderIsNotActive(int warehouseOrderID) {
        StringBuilder errorMessage = new StringBuilder();
        if (new WarehouseOrderTable().checkWarehouseOrderStatus(warehouseOrderID))
            errorMessage.append(String.format(
                    "warehouse order's status with ID %d isn't '0', what means that warehouseOrder is active, but shouldn't", warehouseOrderID));
        return errorMessage.toString();
    }

    private String checkComments() {
        StringBuilder errorMessage = new StringBuilder();
        String expectedComments = StringEscapeUtils.unescapeJava(TestContext.order.getShippingNotes());
        String actualComments = "";
        for (Map.Entry<Integer, Integer> entry : TestContext.warehouseOrders.entrySet()) {
            try {
                actualComments = new WarehouseOrderTable().getColumnValueByPrimaryID(entry.getKey(), "comments");
                return verifyExpectedResults(actualComments, expectedComments);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return errorMessage.toString();
    }

    public void setWarehouseOrder() throws SQLException {
        ResultSet lines = new WarehouseOrderTable().getOrderLines(TestContext.orderID);
        if (TestContext.warehouseOrders == null) {
            TestContext.warehouseOrders = new LinkedHashMap<>();
        }
        while (lines.next()) {
            TestContext.warehouseOrders.put(lines.getInt(1), lines.getInt(10));
        }
    }
}
