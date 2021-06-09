package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;

public class BuyerAccountSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(BuyerAccountSteps.class);

    public String checkBuyerAccountId(LwaTestContext lwaTestContext) {
        StringBuilder errorMessage = new StringBuilder();
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
}
