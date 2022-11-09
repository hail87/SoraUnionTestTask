package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.UserTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;

import java.io.IOException;
import java.sql.SQLException;

public class UserTableSteps {

    private static final Logger logger = LoggerFactory.getLogger(UserTableSteps.class);

    public String checkAllSysUserIDColumn() {
        String expectedAllSysUserID = Context.getTestContext(LwaTestContext.class).getJsonObject().get("seller_username").toString();
        //Remove ["] symbol at the beginning and end of the String.
        expectedAllSysUserID = expectedAllSysUserID.substring(1,expectedAllSysUserID.length() -1 );
        String actualAllSysUserID = null;
        try {
            actualAllSysUserID = new UserTable().getAllSysUserIDValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (expectedAllSysUserID.equalsIgnoreCase(actualAllSysUserID)) {
            logger.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() passed successfully\n + " +
                    "\" orders.orderAllSysID value \nActual: '" +
                                       actualAllSysUserID + "'\nExpected: '" + expectedAllSysUserID + "'");
            return "";
        } else {
            logger.error(new Object(){}.getClass().getEnclosingMethod().getName() + "() not passed\n");
            return "Wrong orders.orderAllSysID value found\nActual: '" +
                    actualAllSysUserID + "'\nExpected: '" + expectedAllSysUserID + "'";
        }
    }
}
