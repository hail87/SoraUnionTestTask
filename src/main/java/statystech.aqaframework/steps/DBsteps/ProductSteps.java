package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.DataObjects.Jackson.OrderItem;
import statystech.aqaframework.TableObjects.ProductTable;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class ProductSteps extends Steps {

    public String checkProduct(OrderItem product) throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkName(product));
        errorMessage.append(checkProductAllSysID(product));
        errorMessage.append(checkSKU(product));
        setProductID(product);
        return errorMessage.toString();
    }

    private String checkName(OrderItem product) throws SQLException {
        String actual = new ProductTable().getColumnValueByProductName(product.getProductName(), "productName");
        String expected = product.getProductName();
        return verifyExpectedResults(actual, expected);
    }

    private String checkProductAllSysID(OrderItem product) throws SQLException {
        String actual = new ProductTable().getColumnValueByProductName(product.getProductName(), "productAllSysID");
        String expected = String.valueOf(product.getProductAllSysID());
        return verifyExpectedResults(actual, expected);
    }

    private String checkSKU(OrderItem product) throws SQLException {
        String actual = new ProductTable().getColumnValueByProductName(product.getProductName(), "productSku");
        String expected = product.getSKU();
        return verifyExpectedResults(actual, expected);
    }

    private void setProductID(OrderItem product) throws SQLException {
        int productID = Integer.parseInt(new ProductTable().getColumnValueByProductName(product.getProductName(), "productID"));
        product.setProductID(productID);
    }

}
