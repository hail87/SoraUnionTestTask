package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

public class ProductParentTable extends TableObject{

    private final String TABLE_NAME = "productParent";

    public ProductParentTable(){
        super.TABLE_NAME = TABLE_NAME;
    }

    public boolean setIsActive(int isActive, int productParentId) {
        return DBUtils.update(String.format("UPDATE %s SET isActive = %d WHERE productParentID = %d", TABLE_NAME, isActive, productParentId));
    }
}
