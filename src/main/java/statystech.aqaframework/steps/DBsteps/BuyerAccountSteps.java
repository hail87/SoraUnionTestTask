package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.BuyerAccountTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;

import java.sql.SQLException;

public class BuyerAccountSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(BuyerAccountSteps.class);

    BuyerAccountTable buyerAccountTable = new BuyerAccountTable();

    public String checkBuyerAccountId(LwaTestContext lwaTestContext) {
        int apiBuyerAccountId = lwaTestContext.getApiBuyerAccountId();
        String dbBuyerAccountID = DBUtils.executeAndReturnString(String.format(
                "select * from buyerAccount where buyerAccountID = '%d'", apiBuyerAccountId));
        logger.info(String.format("buyerAccount '%d' have been found at the BuyerAccount table\n", apiBuyerAccountId));
        if (dbBuyerAccountID.isEmpty()) {
            return String.format("buyerAccount '%d' wasn't been found at the BuyerAccount table!\n", apiBuyerAccountId);
        } else {
            return "";
        }
    }

    public String checkAllSysAccountIDMatch(int buyerAccountId1, int buyerAccountId2) {
        String allSysAccountId1 = "";
        String allSysAccountId2 = "";
        try {
            allSysAccountId1 = buyerAccountTable.getColumnValueByPrimaryID(buyerAccountId1, "ALLSYSAccountID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return String.format("Can't get ALLSYSAccountID value from %s where buyerAccountId = '%d'", buyerAccountTable.getName(), buyerAccountId1);
        }
        try {
            allSysAccountId2 = buyerAccountTable.getColumnValueByPrimaryID(buyerAccountId2, "ALLSYSAccountID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return String.format("Can't get ALLSYSAccountID value from %s where buyerAccountId = '%d'", buyerAccountTable.getName(), buyerAccountId2);
        }
        return verifyExpectedResults(allSysAccountId1,allSysAccountId2);

    }
}
