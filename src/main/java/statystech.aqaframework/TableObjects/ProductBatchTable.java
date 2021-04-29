package statystech.aqaframework.TableObjects;



import statystech.aqaframework.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductBatchTable extends TableObject{

    private final String TABLE_NAME = "shopperGroup";

    public String getLastRowColumnValue(String columnName) throws SQLException {
        return getLastRow(TABLE_NAME).getString(columnName);
    }

    protected ResultSet getLastRow(String tableName) throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where %sID = %d", tableName, TABLE_NAME, getPrimaryID()));
        rs.next();
        return rs;
    }
}
