package statystech.aqaframework.TableObjects;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyerTable extends TableObject {

    private static final Logger logger = LoggerFactory.getLogger(BuyerTable.class);

    private final String TABLE_NAME = "buyer";

    protected ResultSet getLastRow(String tableName) throws SQLException, IOException {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where %sID = %d", tableName, TABLE_NAME, getPrimaryID()));
        rs.next();
        return rs;
    }

    public int getBuyerID() throws SQLException {
        LwaTestContext lwaTestContext = Context.getTestContext(LwaTestContext.class);
        int allSysByuerID = Integer.parseInt(lwaTestContext.getJsonObject()
                .getAsJsonObject("billing_address").get("user_info_id").toString().replace("\"", ""));
        lwaTestContext.setAllSysBuyerID(allSysByuerID);
        return Integer.parseInt(DBUtils.executeAndReturnString(String.format(
                "select %s from %s where allSysBuyerID = '%d'",TABLE_NAME+"ID",TABLE_NAME, allSysByuerID)));
    }

}
