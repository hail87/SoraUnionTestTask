package statystech.aqaframework.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.tests.TestRail.TestRailID;

public abstract class TestClass {

    private static final Logger logger = LoggerFactory.getLogger(TestClass.class);

    @BeforeAll
    static void createContext() {
        Context.initialize();
    }

    @BeforeEach
    public void setTestContext(TestInfo testInfo) {
        TestContext testContext = new TestContext(testInfo.getTestMethod().get().getName());
        Context.addTestContext(testContext);
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
