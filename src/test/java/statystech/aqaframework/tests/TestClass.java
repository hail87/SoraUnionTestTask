package statystech.aqaframework.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;

public abstract class TestClass {

    private static final Logger logger = LoggerFactory.getLogger(TestClass.class);

    @BeforeAll
    static void createContext() {
        Context.initialize();
    }

    @BeforeEach
    public void setTestContext(TestInfo testInfo) {
        int testRailID = getTestRailID(testInfo);
//        boolean isTestRailAnnotationPresent = testInfo.getTestMethod().isPresent()
//                && testInfo.getTestMethod().get().isAnnotationPresent(TestRailID.class);
//        if (isTestRailAnnotationPresent) {
//            testRailID = testInfo.getTestMethod().get().getAnnotation(TestRailID.class).id();
//        }
        if (testRailID != 0) {
            TestContext testContext = new TestContext(testRailID);
            Context.addTestContext(testContext);
        }
    }

    public TestContext getTestContext(TestInfo testInfo) {
        return Context.getTestContext(getTestRailID(testInfo));
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
