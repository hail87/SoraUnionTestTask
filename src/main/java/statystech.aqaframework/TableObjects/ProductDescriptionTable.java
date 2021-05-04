package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

import java.sql.SQLException;

public class ProductDescriptionTable extends TableObject{

    private final String TABLE_NAME = "productDescription";

    public String getProductDescription(int productID) throws SQLException {
        return DBUtils.executeAndReturnString(String.format("select productDescription from %s where productID='%d'", TABLE_NAME, productID));
    }
}
