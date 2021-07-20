package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.AccountAddressTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountAddressSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(AccountAddressSteps.class);

    public AccountAddressTable accountAddressTable = new AccountAddressTable();

    public void setTableRowsQuantity() {
        accountAddressTable.setTableRowsQuantity();
    }

    public String checkShippingAndBillingAddressesID(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkShippingAddressID(lwaTestContext));
        errorMessage.append(checkBillingAddressID(lwaTestContext));
        return errorMessage.toString();
    }

    public String checkRowWithAddressIdBuyerAccountIDAddressTypeCDAndSetAccountAddressID(LwaTestContext lwaTestContext, String addressTypeCD) {
        ResultSet rs = DBUtils.execute(String.format("select * from %s where addressID = '%d' and buyerAccountID = '%d' and addressTypeCD = '%s'",
                accountAddressTable.getName(), lwaTestContext.getOmsShippingAddressID(), lwaTestContext.getApiBuyerAccountId(), addressTypeCD));
        int rowsQuantity = accountAddressTable.getRowsQuantity(rs);
        if (rowsQuantity < 1) {
            return "\nThere was no row with specified parameters found";
        }
        if (rowsQuantity > 1) {
            return "\nThere was more then one row with specified parameters found";
        }
        try {
            rs.cancelRowUpdates();
            rs.next();
            lwaTestContext.setAccountAddressID(Integer.parseInt(rs.getString(1)));
            Context.updateTestContext(lwaTestContext);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public String verifyAddressTypeCD(int primaryID, String expectedAddressTypeCD) {
        String actualAddressTypeCD = "";
        try {
            actualAddressTypeCD = accountAddressTable.getColumnValueByPrimaryID(primaryID, "addressTypeCD");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return verifyExpectedResults(actualAddressTypeCD, expectedAddressTypeCD);
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
                    accountAddressTable.getBillingAddressId(lwaTestContext.getApiBuyerAccountId()),
                    lwaTestContext.getOmsBillingAddressID());
        }
    }
}
