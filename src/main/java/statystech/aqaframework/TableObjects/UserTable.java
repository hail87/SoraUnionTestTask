package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

import java.sql.SQLException;

public class UserTable extends TableObject{

    private final String TABLE_NAME = "user";

    public String getAllSysUserIDValue() throws SQLException {
        return new DBUtils().executeAndReturnString(String.format(
                "select %s from %s where userID = %d", "allSysUserID", TABLE_NAME, new OrdersTable().getUserIDValue()));
    }
}
