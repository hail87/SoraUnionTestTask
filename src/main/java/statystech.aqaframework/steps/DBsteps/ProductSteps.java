package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.TableObjects.ProductTable;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class ProductSteps extends Steps {

    public String checkProduct(Product product) throws SQLException {
        ProductTable productTable = new ProductTable();
        StringBuilder errorMessage = new StringBuilder();
        int productID = new ProductTable().getProductIDbyProductAllSysID(String.valueOf(product.getProductAllSysID()));
        product.setProductID(productID);
        errorMessage.append(verifyExpectedResults(
                productTable.getJsonAndTableValue(productID, "order_items", "product_name")));
        errorMessage.append(checkSKU(product));
        return errorMessage.toString();
    }

    public String getProductDetailsValueFromJSON(String key) {
        return TestContext.JSON_OBJECT.getAsJsonArray("order_items").get(0).getAsJsonObject().get(key)
                .toString().replace("\"", "");
    }

    private String checkSKU(Product product) throws SQLException {
        String actualPhoneNumber = new ProductTable().getColumnValue("productSku");
        String expectedPhoneNumber = product.getProductSKU();
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }

}
