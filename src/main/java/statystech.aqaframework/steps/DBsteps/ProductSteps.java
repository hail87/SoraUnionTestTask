package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.TableObjects.ProductTable;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class ProductSteps extends Steps {

    public String checkProduct(Product product) throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkName(product));
        errorMessage.append(checkProductAllSysID(product));
        errorMessage.append(checkSKU(product));
        setProductID(product);
        return errorMessage.toString();
    }

    private String checkName(Product product) throws SQLException {
        String actual = new ProductTable().getColumnValueByProductName(product.getProductName(), "productName");
        String expected = product.getProductName();
        return verifyExpectedResults(actual, expected);
    }

    private String checkProductAllSysID(Product product) throws SQLException {
        String actual = new ProductTable().getColumnValueByProductName(product.getProductName(), "productAllSysID");
        String expected = String.valueOf(product.getProductAllSysID());
        return verifyExpectedResults(actual, expected);
    }

    private String checkSKU(Product product) throws SQLException {
        String actual = new ProductTable().getColumnValueByProductName(product.getProductName(), "productSku");
        String expected = product.getProductSKU();
        return verifyExpectedResults(actual, expected);
    }

    private void setProductID(Product product) throws SQLException {
        int productID = Integer.parseInt(new ProductTable().getColumnValueByProductName(product.getProductName(), "productID"));
        product.setProductID(productID);
    }

}
