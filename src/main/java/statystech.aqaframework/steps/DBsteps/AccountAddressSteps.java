package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.AccountAddressTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

public class AccountAddressSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(AccountAddressSteps.class);

    public AccountAddressTable accountAddressTable = new AccountAddressTable();

    public void setTableRowsQuantity(){
        accountAddressTable.setTableRowsQuantity();
    }

    public String checkShippingAndBillingAddressesID(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkShippingAddressID(lwaTestContext));
        errorMessage.append(checkBillingAddressID(lwaTestContext));
        return errorMessage.toString();
    }

    public String checkShippingAddressID(LwaTestContext lwaTestContext) {
        if (lwaTestContext.getOmsShippingAddressID() == accountAddressTable.getShippingAddressId(lwaTestContext.getApiBuyerAccountId())) {
            return "";
        } else {
            return String.format(
                    "AddressID '%d' from AccountAddress table is different from ShippingAddressID - '%d' \n " +
                            "P.S.: OmsShippingAddressID or OmsBillingAddressID",
                    accountAddressTable.getShippingAddressId(lwaTestContext.getApiBuyerAccountId()),
                    lwaTestContext.getOmsShippingAddressID());
        }
    }

    public String checkBillingAddressID(LwaTestContext lwaTestContext) {
        if (lwaTestContext.getOmsBillingAddressID() == accountAddressTable.getBillingAddressId(lwaTestContext.getApiBuyerAccountId())) {
            return "";
        } else {
            return String.format(
                    "AddressID '%d' from AccountAddress table is different from BillingAddressID - '%d' \n " +
                            "P.S.: OmsShippingAddressID or OmsBillingAddressID",
                    accountAddressTable.getShippingAddressId(lwaTestContext.getApiBuyerAccountId()),
                    lwaTestContext.getOmsBillingAddressID());
        }
    }
}
