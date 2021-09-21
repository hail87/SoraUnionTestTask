package statystech.aqaframework.tests;

import org.junit.jupiter.api.Test;
import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;

public class TestTestClass {

//    @Test
//    public void downloadAppLogs() {
//        DataUtils.downloadKubeCtlLogs();
//        //DataUtils.downloadKubeCtlLogs();
//    }

    //    @Test
//    public void closeTestRailOpenRuns() {
//        new TestRailSteps().closeAllOpenTestRuns();
//    }

        @Test
    public void cleanDB(){
        try {
            DBUtils.cleanDB("clean_all_lwa_test_data.sql");
            DBUtils.cleanDB("clean_new_billing_address.sql");
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }
}
