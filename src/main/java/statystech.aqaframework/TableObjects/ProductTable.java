package statystech.aqaframework.TableObjects;

import statystech.aqaframework.utils.DBUtils;

public class ProductTable extends TableObject{

    private final String TABLE_NAME = "product";

    public ProductTable(){
        super.TABLE_NAME = TABLE_NAME;
    }

    public boolean changeIsCold(int isCold, String productName){
        return DBUtils.update(String.format("UPDATE %s SET isCold = %d WHERE productName = '%s'", TABLE_NAME, isCold, productName));
    }

}
