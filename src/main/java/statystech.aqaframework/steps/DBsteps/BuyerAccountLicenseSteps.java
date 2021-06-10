package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.BuyerAccountLicenseTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

public class BuyerAccountLicenseSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(BuyerAccountLicenseSteps.class);

    BuyerAccountLicenseTable buyerAccountLicenseTable = new BuyerAccountLicenseTable();

    public String checkBuyerAccountLicenseID(LwaTestContext lwaTestContext) {
        if (!buyerAccountLicenseTable.checkRowWithIDExist(lwaTestContext.getOMSBuyerAccountLicenseID())) {
            return String.format("\nThere is no BuyerAccountLicenseID '%d' found at the BuyerAccountLicense table", lwaTestContext.getOMSBuyerAccountLicenseID());
        }
        return "";
    }

    public String checkBuyerAccountLicenseIDisOnlyOne(LwaTestContext lwaTestContext) {
        if (buyerAccountLicenseTable.getRowsQuantity() > 1) {
            return String.format("\nThere is more than one row found at the BuyerAccountLicense table", lwaTestContext.getOMSBuyerAccountLicenseID());
        }
        if (buyerAccountLicenseTable.getRowsQuantity() < 1) {
            return String.format("\nThere is no rows found at the BuyerAccountLicense table", lwaTestContext.getOMSBuyerAccountLicenseID());
        }
        return "";
    }
}
