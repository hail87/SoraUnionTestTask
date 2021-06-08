package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;

public class AccountAddressTable extends TableObject{

    private final String TABLE_NAME = "accountAddress";

    public int getAddressId(int buyerAccountID) {
        try {
            return Integer.parseInt(DBUtils.executeAndReturnString(String.format("select addressID from %s where buyerAccountID = '%d'", TABLE_NAME, buyerAccountID)));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
}
