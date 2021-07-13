package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

public class OrderStatusHistoryTable extends TableObject{

    private final String TABLE_NAME = "orderStatusHistory";

    public OrderStatusHistoryTable(){
        super.TABLE_NAME = TABLE_NAME;
    }

    public int getOrderStatusID(int orderId) {
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format("select orderStatusID from %s where orderID = '%d'", this.TABLE_NAME, orderId)));
    }
}
