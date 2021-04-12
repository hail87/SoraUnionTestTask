package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.ProductTable;
import statystech.aqaframework.TableObjects.ShippingAddressTable;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class ProductSteps extends Steps {

    public String checkProduct() throws SQLException {
        ProductTable productTable = new ProductTable();
        StringBuilder errorMessage = new StringBuilder();
        int productID = productTable.getProductIDbyProductAllSysID(getProductDetailsValueFromJSON("product_id"));
        errorMessage.append(verifyExpectedResults(
                productTable.getJsonAndTableValue(productID, "order_items", "product_name")));
        errorMessage.append(verifyExpectedResults(
                productTable.getJsonAndTableValue(productID, "order_items", "product_name")));
        errorMessage.append(checkSKU());
        return errorMessage.toString();
    }

    private String getProductDetailsValueFromJSON(String key){
        return TestContext.JSON_OBJECT.getAsJsonArray("order_items").get(0).getAsJsonObject().get(key)
                .toString().replace("\"", "");
    }

    private String checkSKU() throws SQLException {
        String actualPhoneNumber = new ProductTable().getColumnValue("productSku");
        String expectedPhoneNumber = getProductDetailsValueFromJSON("SKU");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }

}
