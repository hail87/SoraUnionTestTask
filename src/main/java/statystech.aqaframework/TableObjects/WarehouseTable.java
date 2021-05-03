package statystech.aqaframework.TableObjects;

import java.sql.SQLException;

public class WarehouseTable extends TableObject {

    private final String TABLE_NAME = "warehouse";

    public int getWarehouseId(String warehouseName) throws SQLException {
        return getPrimaryID("warehouseName", warehouseName);
    }
}
