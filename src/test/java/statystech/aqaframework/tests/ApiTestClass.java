package statystech.aqaframework.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.common.Context.TestContext;
import statystech.aqaframework.utils.DBUtils;

import java.sql.SQLException;

public abstract class ApiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(ApiTestClass.class);

    @BeforeAll
    static void createContext() {
        DBUtils.executeSqlScript("clean_all_lwa_test_data.sql");
        Context.initialize();
    }

    @AfterEach
    public void cleanTestDataAndCloseConnection(TestInfo testInfo) throws SQLException, InterruptedException {
        TestContext testContext = Context.getTestContext(testInfo);
        testContext.closeDbConnection();
        Thread.sleep(500);
        Context.deleteTestContext(testContext);
    }

    public LwaTestContext getLwaTestContext(TestInfo testInfo) {
        return Context.getTestContext(testInfo.getTestMethod().get().getName(), LwaTestContext.class);
    }
}
