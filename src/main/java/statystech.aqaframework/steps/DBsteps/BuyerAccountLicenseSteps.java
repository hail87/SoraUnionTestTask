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
            return String.format("\nThere is no BuyerAccountLicenseID '%d' found at the BuyerAccountLicenseID table", lwaTestContext.getOMSBuyerAccountLicenseID());
        }
        return "";
    }
}
