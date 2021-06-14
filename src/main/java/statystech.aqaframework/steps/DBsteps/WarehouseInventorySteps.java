package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ProductJson.BatchesItem;
import statystech.aqaframework.DataObjects.ProductJson.ItemsItem;
import statystech.aqaframework.TableObjects.WarehouseTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;

public class WarehouseInventorySteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseInventorySteps.class);

    public String checkWarehouseInventory(ItemsItem item) throws SQLException { //verify that table contains only items with batches, and don't contain items without batches
        StringBuilder errorMessage = new StringBuilder();
        if (item.getBatches() != null && item.getBatches().size() > 0) {
            for (BatchesItem batch : item.getBatches()) {
                int warehouseID = new WarehouseTable().getWarehouseId(batch.getCenterName());
                if (DBUtils.executeAndReturnString(String.format(
                        "select * from warehouseInventory where productID = '%s' and warehouseID = '%d'",
                        item.getProductIdFromDB(), warehouseID)).isEmpty()) {
                    errorMessage.append(String.format("There is no line at the warehouseInventory table with productID = '%s' and warehouseID = '%d'",
                            item.getProductIdFromDB(), warehouseID));
                }
            }
        }
        return errorMessage.toString();
    }
}
