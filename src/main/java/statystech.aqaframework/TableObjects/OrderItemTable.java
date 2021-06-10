package statystech.aqaframework.TableObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemTable extends TableObject{

    private static final Logger logger = LoggerFactory.getLogger(OrderItemTable.class);

    private final String TABLE_NAME = "orderItem";

    public OrderItemTable(){
        super.TABLE_NAME = TABLE_NAME;
    }

    public String checkProductIdWithOrderID(int orderID, int expectedProductId) {
        ResultSet rs = DBUtils.execute(String.format("select * from %s where productID = '%d'", TABLE_NAME, expectedProductId));
        try {
            if(!rs.next()){
                logger.error(String.format("There is no line with orderID '%d' and productID '%d'", orderID, expectedProductId));
                return String.format("There is no line with orderID '%d' and productID '%d'", orderID, expectedProductId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.info(String.format("A Row with productID '%d' and orderID '%d' was found successfully", expectedProductId, orderID));
        return "";
    }
}
