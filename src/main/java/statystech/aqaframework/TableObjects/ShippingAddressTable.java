package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShippingAddressTable extends TableObject {

    private final String TABLE_NAME = "shippingAddress";

    public String getColumnValue (String columnName) throws SQLException {
        return getProperRow().getString(columnName);
    }

    private ResultSet getProperRow() throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where shippingAddressID = %d", TABLE_NAME, getPrimaryID()));
        rs.next();
        return rs;
    }

}
