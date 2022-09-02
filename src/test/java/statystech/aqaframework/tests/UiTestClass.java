package statystech.aqaframework.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.tests.TestRail.TestRailID;


public abstract class UiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(ApiTestClass.class);

    @BeforeAll
    static void createContext() {
        Context.initialize();
    }

    @AfterEach
    public void cleanTestDataAndCloseConnection(TestInfo testInfo) throws InterruptedException {
        UiTestContext uiTestContext = getUiTestContext(testInfo);
        uiTestContext.closeWebDriverConnection();
        Thread.sleep(500);
        Context.deleteTestContext(uiTestContext);
        try {
            Context.getTestContext(testInfo, LwaTestContext.class).closeDbConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
