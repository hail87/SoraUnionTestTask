package statystech.aqaframework.TableObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderExceptionHistoryTable extends TableObject{

    private static final Logger logger = LoggerFactory.getLogger(OrderExceptionHistoryTable.class);

    private final String TABLE_NAME = "orderExceptionHistory";

    public OrderExceptionHistoryTable(){
        super.TABLE_NAME = TABLE_NAME;
    }
}
