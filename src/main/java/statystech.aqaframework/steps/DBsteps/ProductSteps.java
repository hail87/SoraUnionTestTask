package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.DataObjects.Jackson.OrderItem;
import statystech.aqaframework.DataObjects.ProductJson.ItemsItem;
import statystech.aqaframework.DataObjects.ProductJson.Product;
import statystech.aqaframework.DataObjects.ProductJson.ProductDto;
import statystech.aqaframework.TableObjects.ProductTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DataUtils;

import java.sql.SQLException;

public class ProductSteps extends Steps {

    public String checkProduct(OrderItem product) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkName(product.getProductName()));
        errorMessage.append(checkProductAllSysID(product));
        errorMessage.append(checkSKU(product));
        setProductID(product);
        return errorMessage.toString();
    }

    public String checkProduct(ItemsItem product) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkName(DataUtils.convertUnicodeToAscii(product.getProductNameEng())));
        errorMessage.append(checkProductAllSysID(product));
        errorMessage.append(checkSKU(product));
        return errorMessage.toString();
    }

    private String checkName(String productName) {
        String actual = null;
        try {
            actual = new ProductTable().getColumnValueByProductName(productName, "productName");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the Product table found";
        }
        return verifyExpectedResults(actual, productName);
    }

    private String checkProductAllSysID(OrderItem product) {
        String actual = null;
        try {
            actual = new ProductTable().getColumnValueByProductName(product.getProductName(), "productAllSysID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "checkProductAllSysID: There is no " + product.getProductName() + " at the Product table found";
        }
        String expected = String.valueOf(product.getProductAllSysID());
        return verifyExpectedResults(actual, expected);
    }

    private String checkSKU(OrderItem product) {
        String actual = null;
        try {
            actual = new ProductTable().getColumnValueByProductName(product.getProductName(), "productSku");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "checkSKU: There is no " + product.getProductName() + " at the Product table found";
        }
        String expected = product.getSKU();
        return verifyExpectedResults(actual, expected);
    }

    private void setProductID(OrderItem product) {
        int productID;
        try {
            productID = Integer.parseInt(new ProductTable().getColumnValueByProductName(product.getProductName(), "productID"));
            product.setProductID(productID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String checkProductAllSysID(ItemsItem product) {
        String actual;
        try {
            actual = new ProductTable().getColumnValueByProductName(DataUtils.convertUnicodeToAscii(product.getProductNameEng()), "productAllSysID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "checkProductAllSysID: There is no " + product.getProductNameEng() + "at the Product table found";
        }
        String expected = String.valueOf(product.getProductId());
        return verifyExpectedResults(actual, expected);
    }

    private String checkSKU(ItemsItem product) {
        String actual = null;
        try {
            actual = new ProductTable().getColumnValueByProductName(DataUtils.convertUnicodeToAscii(product.getProductNameEng()), "productSku");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "checkSKU: There is no " + product.getProductNameEng() + "at the Product table found";
        }
        String expected = product.getProductSku();
        return verifyExpectedResults(actual, expected);
    }

}
