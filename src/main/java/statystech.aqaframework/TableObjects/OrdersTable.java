package statystech.aqaframework.TableObjects;

import com.fasterxml.jackson.core.JsonProcessingException;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersTable extends TableObject {

    private final String TABLE_NAME = "orders";

    public String getOrderAllSysIDValue() {
        return JsonUtils.getValueFromJSON("order_id");
    }

    public int getUserIDValue() throws SQLException, IOException {
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format(
                "select %s from %s where orderAllSysID = '%s'", "userID", TABLE_NAME, Context.getTestContext(LwaTestContext.class).getOrderAllSysID())));
    }

    public String getOrderStatusName() throws SQLException, JsonProcessingException {
        return getProperRow(TABLE_NAME, Integer.parseInt(Context.getTestContext(LwaTestContext.class).getOrder().getOrderId())).getString("orderStatusName");
    }


    public String getOrderStatusID() throws SQLException, JsonProcessingException {
        LwaTestContext lwaTestContext = Context.getTestContext(LwaTestContext.class);
        if(lwaTestContext.getOrder() == null){
            lwaTestContext.makeOrderFromJson();
        }
        return getProperRow(TABLE_NAME, Integer.parseInt(lwaTestContext.getOrder().getOrderId())).getString("orderStatusID");
    }

    public int getShippingAddressIDValue() throws SQLException, IOException {
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format(
                "select %s from %s where orderAllSysID = '%s'", "shippingAddressID", TABLE_NAME, Context.getTestContext(LwaTestContext.class).getOrderAllSysID())));
    }

    public String getCurrencyValue() throws SQLException {
        return getColumnValueByPrimaryID(Integer.parseInt(getOrderAllSysIDValue()), "currency");
    }

    public String getCurrencyConversionValue() throws SQLException {
        return getColumnValueByPrimaryID(Integer.parseInt(getOrderAllSysIDValue()), "conversionUSD");
    }

    public int getShopperGroupIDValue() throws SQLException, IOException {
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format(
                        "select %s from %s where orderAllSysID = '%s'", "shopperGroupID", TABLE_NAME, Context.getTestContext(LwaTestContext.class).getOrderAllSysID())));
    }

    public int getPrimaryID() throws SQLException, IOException {
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format(
                "select orderID from %s where orderAllSysID = \"" + getOrderAllSysIDValue() + "\"", TABLE_NAME)));
    }

    @Override
    protected ResultSet getProperRow(String tableName, int orderAllSysID) throws SQLException {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where orderAllSysID = \"%d\"", tableName, orderAllSysID));
        rs.next();
        return rs;
    }
}
