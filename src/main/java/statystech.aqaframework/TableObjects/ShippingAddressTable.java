package statystech.aqaframework.TableObjects;


import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShippingAddressTable extends TableObject {

    private final String TABLE_NAME = "shippingAddress";

    protected ResultSet getLastRow(String tableName) throws SQLException, IOException {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where %sID = %d", tableName, TABLE_NAME, getPrimaryID()));
        rs.next();
        return rs;
    }

}
