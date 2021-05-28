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

    public String checkFreeStock(ItemsItem item){
        StringBuilder errorMessage = new StringBuilder();
        for (BatchesItem batchesItem : item.getBatches()){
            errorMessage.append(checkFreeStock(batchesItem));
        }
        return errorMessage.toString();
    }
    public String checkFreeStock(BatchesItem item){
        String actual = null;
        try {
            actual = DBUtils.executeAndReturnString(String.format("select freeStock from warehouseBatchInventory where productBatchID = '%s'", item.getProductBatchID()));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return String.format("\n [checkFreeStock]: There is no product with productID %s found at the WarehouseBatchInventory table", item.getId());
        }
        String expected = String.valueOf(item.getQuantity());
        return "\n" + item.getId() + verifyExpectedResults(actual, expected);
    }
}
