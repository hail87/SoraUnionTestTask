package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.DataObjects.Jackson.OrderItem;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.DataObjects.ProductJson.BatchesItem;
import statystech.aqaframework.DataObjects.ProductJson.ItemsItem;
import statystech.aqaframework.TableObjects.ProductBatchTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;


public class ProductBatchSteps extends Steps {

    public String checkBatchNumber(OrderItem product) {

        if (product.getProductID() == 0) {
            return "Should run checkProduct() first!\n";
        }
        ProductBatchTable productBatchTable = new ProductBatchTable();
        String actual = null;
        try {
            actual = productBatchTable.getColumnValueByPrimaryID(
                    productBatchTable.getPrimaryID(
                            "productID", String.valueOf(product.getProductID())), "batchNumber");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "\ncheckBatchNumber: There is no " + product.getProductName() + " at the productBatch table.";
        }
        String expected = product.getWarehouses().get(0).getBatches().get(0).getNumber();
        return verifyExpectedResults(actual, expected);
    }

    public String checkBatchNumber(BatchesItem batch) {
        String actual = null;
        try {
            actual = DBUtils.executeAndReturnString(String.format("select batchNumber from productBatch where allSysBatchID = '%s'", batch.getId()));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return "\ncheckBatchNumber: There is no " + batch.getId() + " allSysBatchID found at the productBatch table.";
        }
        String expected = batch.getNumber();
        return verifyExpectedResults(actual, expected);
    }
}
