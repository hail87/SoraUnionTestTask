package statystech.aqaframework.tests;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.common.Context.TestContext;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.utils.DBUtils;
import java.io.IOException;
import java.sql.SQLException;

public abstract class ApiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(ApiTestClass.class);

    @BeforeAll
    static void createContext() throws IOException, SQLException {
//        DataUtils.clearFolder(new File("allure-results"));
        DBUtils.cleanDB("clean_all_lwa_test_data.sql");
        Context.initialize();
    }

    @AfterEach
    public void cleanTestDataAndCloseConnection(TestInfo testInfo) throws SQLException, IOException, InterruptedException {
        TestContext testContext = Context.getTestContext(testInfo);
        testContext.closeDbConnection();
        Thread.sleep(500);
        Context.deleteTestContext(testContext);
    }

    public LwaTestContext getLwaTestContext(TestInfo testInfo) {
        return Context.getTestContext(testInfo.getTestMethod().get().getName(), LwaTestContext.class);
    }

    public UiTestContext getUiTestContext(TestInfo testInfo) {
        return Context.getTestContext(testInfo.getTestMethod().get().getName(), UiTestContext.class);
    }

    public int getTestRailID(TestInfo testInfo) {
        int testRailID = 0;
        boolean isTestRailAnnotationPresent = testInfo.getTestMethod().isPresent()
                && testInfo.getTestMethod().get().isAnnotationPresent(TestRailID.class);
        if (isTestRailAnnotationPresent) {
            testRailID = testInfo.getTestMethod().get().getAnnotation(TestRailID.class).id();
            logger.info("testRailID: " + testRailID);
        }
        if (testRailID == 0) {
            logger.warn("testRailID not found");
        }
        return testRailID;
    }


}
