package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersTable extends TableObject {

    private final String TABLE_NAME = "orders";

    public String getOrderAllSysIDValue() {
        return DataUtils.getValueFromJSON("order_id");
    }

    public int getUserIDValue() throws SQLException {
        return Integer.parseInt(new DBUtils().select(TABLE_NAME, "userID"));
    }

    public int getShippingAddressIDValue() throws SQLException {
        return Integer.parseInt(new DBUtils().select(TABLE_NAME, "shippingAddressID"));
    }

    public String getCurrencyValue() throws SQLException {
        return getColumnValue(Integer.parseInt(getOrderAllSysIDValue()), "currency");
    }

    public String getCurrencyConversionValue() throws SQLException {
        return getColumnValue(Integer.parseInt(getOrderAllSysIDValue()), "conversionUSD");
    }

    public int getShopperGroupIDValue() throws SQLException {
        return Integer.parseInt(new DBUtils().select(TABLE_NAME, "shopperGroupID"));
    }

    public int getPrimaryID() throws SQLException {
        return Integer.parseInt(new DBUtils().executeAndReturnString(String.format(
                "select orderID from %s where orderAllSysID = \"" + getOrderAllSysIDValue() + "\"", TABLE_NAME)));
    }

    @Override
    protected ResultSet getProperRow(String tableName, int orderAllSysID) throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where orderAllSysID = \"%d\"", tableName, orderAllSysID));
        rs.next();
        return rs;
    }
}
