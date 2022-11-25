package statystech.aqaframework.steps.DBsteps;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ProductJson.ProductImport;
import statystech.aqaframework.TableObjects.ProductParentTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DataUtils;

import java.sql.SQLException;

public class ProductParentSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(ProductParentSteps.class);

    public String checkProduct(LwaTestContext lwaTestContext) {
        ProductImport productImport = lwaTestContext.getProductImport();
        Assert.assertNotNull(productImport);
        StringBuilder errorMessage = new StringBuilder();

        errorMessage.append(checkName(productImport.getProductName()));
        errorMessage.append(checkCatalogCategory(lwaTestContext));
        errorMessage.append(checkIsLicensed(lwaTestContext));
        errorMessage.append(checkDescription(lwaTestContext));
        return errorMessage.toString();
    }

    private String checkCatalogCategory(LwaTestContext lwaTestContext) {
        String actual;
        String productName = lwaTestContext.getProductImport().getProductName();
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "catalogCategory");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the Product table found";
        }
        return verifyExpectedResults(actual, lwaTestContext.getProductImport().getCatalogCategory());
    }

    private String checkIsLicensed(LwaTestContext lwaTestContext) {
        String actual;
        String productName = lwaTestContext.getProductImport().getProductName();
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "isLicensed");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the Product table found";
        }
        switch (lwaTestContext.getProductImport().getCatalogCategory()) {
            case "true":
                return verifyExpectedResults(actual, "1");
            case "false":
                return verifyExpectedResults(actual, "0");
            default:
                return "\nSomething went wrong during checkIsLicensed method\n";
        }
    }

    private String checkDescription(LwaTestContext lwaTestContext) {
        String actual;
        String productName = lwaTestContext.getProductImport().getProductName();
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "productShortDescription");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the Product table found";
        }
        return verifyExpectedResults(actual, lwaTestContext.getProductImport().getDescription());
    }

    private String checkName(String productName) {
        String actual = null;
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "productName");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the Product table found";
        }
        return verifyExpectedResults(DataUtils.removeUnicode(actual), DataUtils.removeUnicode(productName));
    }
}
