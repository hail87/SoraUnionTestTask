package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

import java.sql.SQLException;

public class OrdersTable extends TableObject {

    private final String TABLE_NAME = "orders";

    public String getOrderAllSysIDValue() throws SQLException {
        return new DBUtils().select(TABLE_NAME,  "orderAllSysID");
    }
}
