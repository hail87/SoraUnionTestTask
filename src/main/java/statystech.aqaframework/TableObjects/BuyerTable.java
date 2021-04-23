package statystech.aqaframework.TableObjects;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.DBsteps.OrdersSteps;
import statystech.aqaframework.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyerTable extends TableObject {

    private static final Logger logger = LoggerFactory.getLogger(BuyerTable.class);

    private final String TABLE_NAME = "buyer";

    protected ResultSet getLastRow(String tableName) throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where %sID = %d", tableName, TABLE_NAME, getPrimaryID()));
        rs.next();
        return rs;
    }

    public int getBuyerID() throws SQLException {
        int allSysByuerID = Integer.parseInt(TestContext.JSON_OBJECT
                .getAsJsonObject("billing_address").get("user_info_id").toString().replace("\"", ""));
        TestContext.allSysBuyerID = allSysByuerID;
        try {
            return Integer.parseInt(new DBUtils().executeAndReturnString(String.format(
                    "select %s from %s where allSysBuyerID = '%d'",TABLE_NAME+"ID",TABLE_NAME, allSysByuerID)));
        } catch (SQLException throwables) {
            logger.error("!!! There is no line with specified allSysBuyerID exist: " + allSysByuerID + "\nTEST EXIT !!!");
            throw new SQLException();
        }
    }

}
