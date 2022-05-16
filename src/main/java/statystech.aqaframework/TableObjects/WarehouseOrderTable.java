package statystech.aqaframework.TableObjects;


import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarehouseOrderTable extends TableObject {

    private final String TABLE_NAME = "warehouseOrder";

    public WarehouseOrderTable(){
        super.TABLE_NAME = TABLE_NAME;
    }

    public String getLastRowColumnValue(String columnName) throws SQLException {
        String result = "";
        try {
            result = getLastRow(TABLE_NAME).getString(columnName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected ResultSet getLastRow(String tableName) throws SQLException, IOException {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where %sID = %d", tableName, TABLE_NAME, getPrimaryID()));
        rs.next();
        return rs;
    }

    public ResultSet getOrderLines(int orderID){
        return DBUtils.execute(String.format("select * from %s where orderID='%d'", TABLE_NAME, orderID));
    }

    public boolean checkWarehouseOrderStatus(int warehouseOrderId) {
        return "1".equalsIgnoreCase(
                String.valueOf(DBUtils.executeAndReturnString(String.format(
                        "select warehouseOrderStatusID from %s where warehouseOrderID='%d'", TABLE_NAME, warehouseOrderId))));
    }
}
