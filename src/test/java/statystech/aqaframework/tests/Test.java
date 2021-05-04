package statystech.aqaframework.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.tests.TestRail.TestRailID;

public abstract class Test {

    @BeforeAll
    static void createContext(){
        Context.initialize();
    }

    @BeforeEach
    public void setTestContext(TestInfo testInfo){
        int testRailID = testInfo.getTestMethod().get().getAnnotation(TestRailID.class).id();
        if (testRailID != 0) {
            TestContext testContext = new TestContext(testRailID);
            Context.addTestContext(testContext);
        } else {
            throw new NullPointerException("testRailID not found");
        }
    }
}
