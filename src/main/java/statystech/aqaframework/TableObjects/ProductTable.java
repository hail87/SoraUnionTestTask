package statystech.aqaframework.TableObjects;

import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductTable extends TableObject{

    private final String TABLE_NAME = "product";

    public String getColumnValue (String columnName) throws SQLException {
        return getLastRow(TABLE_NAME).getString(columnName);
    }

    protected ResultSet getLastRow(String tableName) throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where %sID = %d", tableName, TABLE_NAME, getPrimaryID()));
        rs.next();
        return rs;
    }

    public int getProductIDbyProductAllSysID(String productAllSysID) throws SQLException {
        return Integer.parseInt(
                new DBUtils().executeAndReturnString(String.format(
                        "select %sID from %s where %s = %s", TABLE_NAME, TABLE_NAME,"productAllSysID",productAllSysID))
        );
    }
}
