package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

public class AccountAddressTable extends TableObject {

    private final String TABLE_NAME = "accountAddress";

    public AccountAddressTable(){
        super.TABLE_NAME = TABLE_NAME;
    }

    public int getShippingAddressId(int buyerAccountID) {
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format("select addressID from %s where addressTypeCD = 'SA' and buyerAccountID = '%d'", TABLE_NAME, buyerAccountID)));
    }

    public int getBillingAddressId(int buyerAccountID) {
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format("select addressID from %s where addressTypeCD = 'BA' and buyerAccountID = '%d'", TABLE_NAME, buyerAccountID)));
    }
}
