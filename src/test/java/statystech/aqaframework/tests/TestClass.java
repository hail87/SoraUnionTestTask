package statystech.aqaframework.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;

public abstract class TestClass {

    private static final Logger logger = LoggerFactory.getLogger(TestClass.class);

    @BeforeAll
    static void createContext() throws IOException, SQLException {
        DBUtils.cleanDB("clean_all_lwa_test_data.sql");
        Context.initialize();
    }

    @BeforeEach
    public void setTestContext(TestInfo testInfo) throws SQLException, IOException {
        TestContext testContext = new TestContext(testInfo.getTestMethod().get().getName());
        testContext.getConnection();
        Context.addTestContext(testContext);
    }

    @AfterEach
    public void cleanTestDataAndCloseConnection(TestInfo testInfo) throws SQLException, IOException, InterruptedException {
        TestContext testContext = Context.getTestContext(testInfo);
        testContext.closeConnection();
        Thread.sleep(500);
        Context.deleteTestContext(testContext);
    }

    public TestContext getTestContext(TestInfo testInfo) {
        return Context.getTestContext(testInfo.getTestMethod().get().getName());
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
