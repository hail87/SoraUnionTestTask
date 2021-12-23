package statystech.aqaframework.steps.DBsteps;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OrderJackson.OrderItem;
import statystech.aqaframework.DataObjects.ProductJson.BatchesItem;
import statystech.aqaframework.DataObjects.ProductJson.ItemsItem;
import statystech.aqaframework.TableObjects.ProductBatchTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;

import java.sql.SQLException;


public class ProductBatchSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(ProductBatchSteps.class);

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
        return verifyExpectedResults(DataUtils.decrypt(actual), expected);
    }

    public String checkBatchNumberIsPresent(BatchesItem batch) {
        if (!new ProductBatchTable().checkRowWithValueIsPresent("batchNumber", DataUtils.encrypt(batch.getNumber()))){
            return String.format("\nThere is no batchNumber '%s' found at the productBatch table\n", batch.getNumber());
        }
        return "";
    }

    public String getProductBatchID(int productID) throws SQLException {
        return new ProductBatchTable().getColumnValueByColumnValue("productBatchID","productID", String.valueOf(productID));
    }

    public String setProductBatchID(ItemsItem item) {
        StringBuilder errorMessage = new StringBuilder();
        for (BatchesItem batchesItem : item.getBatches()) {
            errorMessage.append(setProductBatchID(batchesItem));
        }
        return errorMessage.toString();
    }

    public String setProductBatchID(BatchesItem batch) {
        String productBatchId = DBUtils.executeAndReturnString(String.format("select productBatchID from productBatch where batchNumber = '%s'", DataUtils.encrypt(batch.getNumber())));
        if (productBatchId.isEmpty()) {
            String error = "\ncheckBatchNumber: There is no " + batch.getNumber() + " batchNumber found at the productBatch table.";
            logger.error(error);
            return error;
        }
        batch.setProductBatchID(productBatchId);
        return "";
    }

    public String checkProductBatchIsPresent(String allSysBatchID) {
        String result = DBUtils.executeAndReturnString(String.format("select productBatchID from productBatch where allSysBatchID = '%s'", allSysBatchID));
        if (result.isEmpty()) {
            String error = "\ncheckBatchNumber: There is no " + allSysBatchID + " allSysBatchID found at the productBatch table.";
            logger.error(error);
            return error;
        }
        return "";
    }
}
