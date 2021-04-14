package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.TableObjects.ProductBatchTable;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;


public class ProductBatchSteps extends Steps {

    public String checkBatchNumber(Product product) throws SQLException {
        ProductBatchTable productBatchTable = new ProductBatchTable();
        String actualPhoneNumber = productBatchTable.getColumnValue(
                productBatchTable.getPrimaryID(
                        "productID", String.valueOf(product.getProductID())), "batchNumber");
        String expectedPhoneNumber = product.getBatchNumber();
                //JsonUtils.getValueFromJSON("order_items", "ff_centers", "batches", "number");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }
}
