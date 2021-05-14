package statystech.aqaframework.TableObjects;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;
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
        TestContext testContext = Context.getTestContext();
        int allSysByuerID = Integer.parseInt(testContext.getJsonObject()
                .getAsJsonObject("billing_address").get("user_info_id").toString().replace("\"", ""));
        testContext.setAllSysBuyerID(allSysByuerID);
        try {
            return Integer.parseInt(DBUtils.executeAndReturnString(String.format(
                    "select %s from %s where allSysBuyerID = '%d'",TABLE_NAME+"ID",TABLE_NAME, allSysByuerID)));
        } catch (SQLException | IOException throwables) {
            logger.error("!!! There is no line with specified allSysBuyerID exist: " + allSysByuerID + "\nTEST EXIT !!!");
            throw new SQLException();
        }
    }

}
