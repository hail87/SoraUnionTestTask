package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.AccountAddressTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

public class AccountAddressSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(AccountAddressSteps.class);

    AccountAddressTable accountAddressTable = new AccountAddressTable();

    public String checkAddressID(LwaTestContext lwaTestContext) {
        if(lwaTestContext.getOmsShippingAddressID() == accountAddressTable.getAddressId(lwaTestContext.getApiBuyerAccountId())){
            return "";
        } else {
            return String.format(
                    "AddressID '%d' from AccountAddress table is different from OmsShippingAddressID '%d'",
                    accountAddressTable.getAddressId(lwaTestContext.getApiBuyerAccountId()),lwaTestContext.getOmsShippingAddressID());
        }
    }
}
