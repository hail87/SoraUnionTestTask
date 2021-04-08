package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class shippingAddressTable extends TableObject {

    private final String TABLE_NAME = "shippingAddress";

    private ResultSet getProperRow(int shippingAddressID) throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where shippingAddressID = %d", TABLE_NAME, shippingAddressID));
        rs.next();
        return rs;
    }

    public String getColumnValue (int shippingAddressID, String columnName) throws SQLException {
        return getProperRow(shippingAddressID).getString(columnName);
    }

}
