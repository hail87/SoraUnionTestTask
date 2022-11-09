package statystech.aqaframework.TableObjects;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrdersTable extends TableObject {

    private static final Logger logger = LoggerFactory.getLogger(OrdersTable.class);

    final String TABLE_NAME = "orders";

    public OrdersTable() {
        super.TABLE_NAME = TABLE_NAME;
    }

    public String getOrderAllSysIDValue() {
        return JsonUtils.getValueFromJSON("order_id");
    }

    public int getUserIDValue() {
        var result = DBUtils.executeAndReturnString(String.format(
                "select %s from %s where orderAllSysID = '%s'",
                "userID",
                TABLE_NAME,
                Context.getTestContext(LwaTestContext.class).getOrderAllSysID()));
        if (result == null) {
            logger.error("\norders.userID is null, but shouldn't be");
            assertNotNull(result);
        }
        return Integer.parseInt(result);
    }

    public String getOrderStatusName() throws SQLException {
        return getProperRow(TABLE_NAME, Integer.parseInt(Context.getTestContext(LwaTestContext.class).getOrder().getOrderId())).getString("orderStatusName");
    }


    public String getOrderStatusID() throws SQLException, JsonProcessingException {
        LwaTestContext lwaTestContext = Context.getTestContext(LwaTestContext.class);
        if (lwaTestContext.getOrder() == null) {
            lwaTestContext.makeOrderFromJson();
        }
        return getProperRow(TABLE_NAME, Integer.parseInt(lwaTestContext.getOrder().getOrderId())).getString("orderStatusID");
    }

    public int getShippingAddressIDValue() {
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format(
                "select %s from %s where orderAllSysID = '%s'", "shippingAddressID", TABLE_NAME, Context.getTestContext(LwaTestContext.class).getOrderAllSysID())));
    }

    public String getCurrencyValue() throws SQLException {
        return getColumnValueByPrimaryID(Integer.parseInt(getOrderAllSysIDValue()), "currency");
    }

    public String getCurrencyConversionValue() throws SQLException {
        return getColumnValueByPrimaryID(Integer.parseInt(getOrderAllSysIDValue()), "conversionUSD");
    }

    public int getShopperGroupIDValue() throws IOException {
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format(
                "select %s from %s where orderAllSysID = '%s'", "shopperGroupID", TABLE_NAME, Context.getTestContext(LwaTestContext.class).getOrderAllSysID())));
    }

    public int getPrimaryID() throws SQLException, IOException {

        return Integer.parseInt(DBUtils.executeAndReturnString(String.format(
                "select orderID from %s where orderAllSysID = \"" + getOrderAllSysIDValue() + "\"", TABLE_NAME)));
    }

    public int getBuyerAccountId(int primaryId) {
        String response = DBUtils.executeAndReturnString("select buyerAccountID from orders where orderID = '" + primaryId + "'");
        if (!response.isEmpty()) {
            return Integer.parseInt(response);
        } else {
            throw new NumberFormatException();
        }
    }

    public int getOMSShippingAddressID(int primaryId) {
        return Integer.parseInt(DBUtils.executeAndReturnString("select OMSShippingAddressID from orders where orderID = '" + primaryId + "'"));
    }

    public int getOMSBillingAddressID(int primaryId) {
        return Integer.parseInt(DBUtils.executeAndReturnString("select OMSBillingAddressID from orders where orderID = '" + primaryId + "'"));
    }

    public int getOMSBuyerAccountLicenseID(int primaryId) {
        return Integer.parseInt(DBUtils.executeAndReturnString("select OMSBuyerAccountLicenseID from orders where orderID = '" + primaryId + "'"));
    }

    public int getPaymentMethodID(int primaryId) {
        return Integer.parseInt(DBUtils.executeAndReturnString("select paymentMethodID from orders where orderID = '" + primaryId + "'"));
    }

    public String getOMSisPaid(int orderId) {
        String omsIsPaid = "";
        omsIsPaid = DBUtils.executeAndReturnString("select OMSIsPaid from orders where orderID = '" + orderId + "'");
        return omsIsPaid;
    }

    public boolean setOMSIsPaid(int isPaid, int orderId) {
        return DBUtils.update(String.format("UPDATE %s SET OMSIsPaid = %d WHERE orderID = %d", TABLE_NAME, isPaid, orderId));
    }

    @Override
    protected ResultSet getProperRow(String tableName, int orderAllSysID) {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where orderAllSysID = \"%d\"", tableName, orderAllSysID));
        try {
            rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }
}
