package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.TableObjects.ProductBatchTable;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;


public class ProductBatchSteps extends Steps {

    public String checkBatchNumber(Product product) throws SQLException {

        if (product.getProductID() == 0) {
            return "Should run checkProduct() first!\n";
        }
        ProductBatchTable productBatchTable = new ProductBatchTable();
        String actual = productBatchTable.getColumnValue(
                productBatchTable.getPrimaryID(
                        "productID", String.valueOf(product.getProductID())), "batchNumber");
        String expected = product.getWarehouses().get(0).getBatches().get(0).getNumber();
        return verifyExpectedResults(actual, expected);
    }
}
