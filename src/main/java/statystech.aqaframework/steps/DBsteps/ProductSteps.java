package statystech.aqaframework.steps.DBsteps;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OrderJackson.OrderItem;
import statystech.aqaframework.DataObjects.ProductJson.ItemsItem;
import statystech.aqaframework.TableObjects.ProductTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;

import java.sql.SQLException;

public class ProductSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(ProductSteps.class);

    ProductTable productTable = new ProductTable();

    public String changeIsCold(int isCold, String productName){
        if (productTable.changeIsCold(isCold, productName)){
            return "";
        } else {
            return "\nproduct.isCold wasn't change!\n";
        }
    }

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
        errorMessage.append(checkProductUnavailable(product));
        setProductID(product);
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
        return verifyExpectedResults(DataUtils.removeFuckingUnicode(actual), DataUtils.removeFuckingUnicode(productName));
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

    private void setProductID(ItemsItem item) {
        String productID;
        try {
            productID = new ProductTable().getColumnValueByProductName(item.getProductNameEng(), "productID");
            item.setProductIdFromDB(productID);
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

    public String checkProductUnavailable(ItemsItem item) {
        String expected = item.getProductUnavailable().equalsIgnoreCase("Y") ? "1" : "0";
        String actual = null;
        String productName = item.getProductNameEng();
        try {
            //actual = new ProductTable().getColumnValueByProductName(DataUtils.convertUnicodeToAscii(item.getProductNameEng()), "productUnavailable");
            actual = new ProductTable().getColumnValueByProductName(productName, "productUnavailable");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "[checkProductUnavailable]: There is no '" + productName + "' at the Product table found";
        }
        return verifyExpectedResults(actual, expected);
    }

}
