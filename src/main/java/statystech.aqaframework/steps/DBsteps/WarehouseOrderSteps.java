package statystech.aqaframework.steps.DBsteps;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringEscapeUtils;
import statystech.aqaframework.TableObjects.WarehouseOrderTable;
import statystech.aqaframework.TableObjects.WarehouseTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WarehouseOrderSteps extends Steps {

    @SneakyThrows
    public WarehouseOrderSteps() {
        setWarehouseOrders();
    }

    public String checkWarehouseOrderTable() throws SQLException, IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkWarehouseOrderStatusesIsActive());
        errorMessage.append(checkComments());
        return errorMessage.toString();
    }

    public String checkWarehouseOrderQuantity(int warehouseOrderQuantity) {
        StringBuilder errorMessage = new StringBuilder();
        if (Context.getTestContext(LwaTestContext.class).getWarehouseOrders().size() != warehouseOrderQuantity) {
            errorMessage.append(String.format(
                    "There is no %d but %d warehouseOrders detected", warehouseOrderQuantity, Context.getTestContext(LwaTestContext.class).getWarehouseOrders().size()));
        }
        return errorMessage.toString();
    }

    public String checkWarehouseOrderStatusesIsActive() throws SQLException, IOException {
        StringBuilder errorMessage = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : Context.getTestContext(LwaTestContext.class).getWarehouseOrders().entrySet()) {
            if (!new WarehouseOrderTable().checkWarehouseOrderStatus(entry.getKey()))
                errorMessage.append(String.format(
                        "warehouse order's status with ID %d isn't '1', what means that warehouseOrder is not active\n", entry.getKey()));
        }
        return errorMessage.toString();
    }

    public String checkWarehouseOrderIsNotActive(String warehouseName) throws SQLException, IOException {
        int wareHouseId = new WarehouseTable().getWarehouseId(warehouseName);
        int warehouseOrderID = Context.getTestContext(LwaTestContext.class).getWarehouseOrders().entrySet().stream().filter(p -> p.getValue().equals(wareHouseId)).findFirst().orElse(null).getKey();
        return checkWarehouseOrderIsNotActive(warehouseOrderID);
    }

    private String checkWarehouseOrderIsNotActive(int warehouseOrderID) throws SQLException, IOException {
        StringBuilder errorMessage = new StringBuilder();
        if (new WarehouseOrderTable().checkWarehouseOrderStatus(warehouseOrderID))
            errorMessage.append(String.format(
                    "warehouse order's status with ID %d isn't '0', what means that warehouseOrder is active, but shouldn't\n", warehouseOrderID));
        return errorMessage.toString();
    }

    private String checkComments() {
        StringBuilder errorMessage = new StringBuilder();
        String expectedComments = StringEscapeUtils.unescapeJava(Context.getTestContext(LwaTestContext.class).getOrder().getShippingNotes());
        String actualComments = "";
        for (Map.Entry<Integer, Integer> entry : Context.getTestContext(LwaTestContext.class).getWarehouseOrders().entrySet()) {
            try {
                actualComments = new WarehouseOrderTable().getColumnValueByPrimaryID(entry.getKey(), "comments");
                return verifyExpectedResults(StringEscapeUtils.unescapeJava(actualComments), expectedComments);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return errorMessage.toString();
    }

    public void setWarehouseOrders() throws SQLException {
        LwaTestContext lwaTestContext = Context.getTestContext(LwaTestContext.class);
        ResultSet lines = new WarehouseOrderTable().getOrderLines(lwaTestContext.getOrderID());
        if (lwaTestContext.getWarehouseOrders() == null) {
            lwaTestContext.setWarehouseOrders(new LinkedHashMap<>());
        }
        while (lines.next()) {
            LinkedHashMap<Integer, Integer> warehouseOrders = lwaTestContext.getWarehouseOrders();
            warehouseOrders.put(lines.getInt(1), lines.getInt(10));
            lwaTestContext.setWarehouseOrders(warehouseOrders);
        }
        Context.updateTestContext(lwaTestContext);
    }

    public int getWarehouseId(int warehouseOrderId) throws SQLException {
        return Integer.parseInt(new WarehouseOrderTable().getColumnValueByPrimaryID(warehouseOrderId, "warehouseID"));
    }

    public int getWarehouseOrderId(int orderId) throws SQLException {
        return Integer.parseInt(new WarehouseOrderTable().getColumnValueByColumnValue("warehouseOrderID", "orderID", String.valueOf(orderId)));
    }
}
