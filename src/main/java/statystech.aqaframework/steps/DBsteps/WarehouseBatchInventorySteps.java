package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ProductJson.BatchesItem;
import statystech.aqaframework.DataObjects.ProductJson.ItemsItem;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;


import java.io.IOException;
import java.sql.SQLException;

public class WarehouseBatchInventorySteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseBatchInventorySteps.class);

    public String checkFreeStock(ItemsItem item) {
        StringBuilder errorMessage = new StringBuilder();
        for (BatchesItem batchesItem : item.getUniqueBatches()) {
            errorMessage.append(checkFreeStock(batchesItem));
        }
        return errorMessage.toString();
    }

    public String checkFreeStock(BatchesItem item) {
        String dbFreeStock = DBUtils.executeAndReturnString(String.format("select freeStock from warehouseBatchInventory where productBatchID = '%s'", item.getProductBatchID()));
        if (dbFreeStock.isEmpty()) {
            return String.format("\n [checkFreeStock]: There is no product with productBatchID '%s' found at the WarehouseBatchInventory table", item.getProductBatchID());
        }
        String expected = String.valueOf(item.getQuantity());
        String result = verifyExpectedResults(dbFreeStock, expected);
        if (!result.isEmpty()){
            return "productBatchID '" + item.getProductBatchID() + "' : " + result;
        }
        return "";
    }
}
