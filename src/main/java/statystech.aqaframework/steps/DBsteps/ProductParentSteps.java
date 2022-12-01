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
        errorMessage.append(checkBrandName(lwaTestContext));
        errorMessage.append(checkHsCode(lwaTestContext));
        errorMessage.append(checkManufacturer(lwaTestContext));
        errorMessage.append(checkTemperatureDetails(lwaTestContext));
        errorMessage.append(checkIsCold(lwaTestContext));
        errorMessage.append(checkIsGenericFlag(lwaTestContext));
        errorMessage.append(checkIsActive(lwaTestContext));
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
        if (lwaTestContext.getProductImport().isLicensed()) {
            return verifyExpectedResults(actual, "1");
        } else {
            return verifyExpectedResults(actual, "0");
        }
    }

    private String checkDescription(LwaTestContext lwaTestContext) {
        String actual;
        String productName = lwaTestContext.getProductImport().getProductName();
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "productDescription");
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
            return "There is no " + productName + " at the productParent table found";
        }
        return verifyExpectedResults(DataUtils.removeUnicode(actual), DataUtils.removeUnicode(productName));
    }

    private String checkBrandName(LwaTestContext lwaTestContext) {
        String actual = null;
        String productName = lwaTestContext.getProductImport().getProductName();
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "brandName");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the productParent table found";
        }
        return verifyExpectedResults(DataUtils.removeUnicode(actual),
                DataUtils.removeUnicode(lwaTestContext.getProductImport().getBrandName()));
    }

    private String checkManufacturer(LwaTestContext lwaTestContext) {
        String actual = null;
        String productName = lwaTestContext.getProductImport().getProductName();
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "manufacturer");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the productParent table found";
        }
        return verifyExpectedResults(DataUtils.removeUnicode(actual),
                DataUtils.removeUnicode(lwaTestContext.getProductImport().getManufacturer()));
    }

    private String checkHsCode(LwaTestContext lwaTestContext) {
        String actual = null;
        String productName = lwaTestContext.getProductImport().getProductName();
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "hsCode");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the productParent table found";
        }
        return verifyExpectedResults(DataUtils.removeUnicode(actual), lwaTestContext.getProductImport().getHsCode());
    }

    private String checkTemperatureDetails(LwaTestContext lwaTestContext) {
        String actual = null;
        String productName = lwaTestContext.getProductImport().getProductName();
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "coolingDetails");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the productParent table found";
        }
        return verifyExpectedResults(DataUtils.removeUnicode(actual), lwaTestContext.getProductImport().getTemperatureDetails());
    }

    private String checkIsCold(LwaTestContext lwaTestContext) {
        String productName = lwaTestContext.getProductImport().getProductName();
        String actual;
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "isCold");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the productParent table found";
        }
        if (lwaTestContext.getProductImport().isCold()) {
            return verifyExpectedResults(actual, "1");
        } else {
            return verifyExpectedResults(actual, "0");
        }
    }

    private String checkIsGenericFlag(LwaTestContext lwaTestContext) {
        String productName = lwaTestContext.getProductImport().getProductName();
        String actual = null;
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "isGenericFlag");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the productParent table found";
        }
        if (lwaTestContext.getProductImport().isGeneric()) {
            return verifyExpectedResults(actual, "1");
        } else {
            return verifyExpectedResults(actual, "0");
        }
    }

    private String checkIsActive(LwaTestContext lwaTestContext) {
        String productName = lwaTestContext.getProductImport().getProductName();
        String actual = null;
        try {
            actual = new ProductParentTable().getColumnValueByProductName(productName, "isActive");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "There is no " + productName + " at the productParent table found";
        }
        if (lwaTestContext.getProductImport().isActive()) {
            return verifyExpectedResults(actual, "1");
        } else {
            return verifyExpectedResults(actual, "0");
        }
    }
}