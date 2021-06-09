package statystech.aqaframework.TableObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderItemTable extends TableObject{

    private static final Logger logger = LoggerFactory.getLogger(OrderItemTable.class);

    private final String TABLE_NAME = "orderItem";

    public OrderItemTable(){
        super.TABLE_NAME = TABLE_NAME;
    }
}
