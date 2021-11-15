package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderStatusHistoryTable extends TableObject{

    private final String TABLE_NAME = "orderStatusHistory";

    public OrderStatusHistoryTable(){
        super.TABLE_NAME = TABLE_NAME;
    }

    public int getOrderStatusID(int orderId) {
        ResultSet rs = DBUtils.executeUpdatable(String.format("select orderStatusID from %s where orderID = '%d'", this.TABLE_NAME, orderId));
        String result = "";
        try {
            rs.last();
            result = rs.getString(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Integer.parseInt(result);
    }
}
