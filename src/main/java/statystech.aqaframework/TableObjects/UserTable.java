package statystech.aqaframework.TableObjects;


import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTable extends TableObject {

    private final String TABLE_NAME = "user";

    public UserTable() {
        super.TABLE_NAME = TABLE_NAME;
    }

    public String getAllSysUserIDValue() throws IOException {
        return DBUtils.executeAndReturnString(String.format(
                "select %s from %s where userID = %d", "allSysUserID", TABLE_NAME, new OrdersTable().getUserIDValue()));
    }
}
