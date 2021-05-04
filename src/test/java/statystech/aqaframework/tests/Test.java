package statystech.aqaframework.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.tests.TestRail.TestRailID;

import java.util.HashSet;

public abstract class Test {

    @BeforeAll
    static void createContext(){
        Context.context = new HashSet<>();
    }

    @BeforeEach
    public void setTestContext(TestInfo testInfo){
        int testRailID = testInfo.getTestMethod().get().getAnnotation(TestRailID.class).id();
        if (testRailID != 0) {
            TestContext testContext = new TestContext(testRailID);
            Context.context.add(testContext);
        } else {
            throw new NullPointerException("testRailID not found");
        }
    }
}
