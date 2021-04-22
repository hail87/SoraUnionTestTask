package statystech.aqaframework.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import statystech.aqaframework.tests.TestRail.TestSuiteID;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;

public class TestRunner {

    @Test
    public void testRunner() throws IOException {
//        String runID = System.getProperty( "runID" );
//        String suiteID = System.getProperty( "suiteID" );
        String runID = "7";
        String suiteID = "1";
        System.out.println("runID: " + runID);
        System.out.println("suiteID: " + suiteID);
        DataUtils.updateTestRailPropertyParameter("testrail_runId", runID);
        DataUtils.updateTestRailPropertyParameter("testrail_testSuiteId", suiteID);
        Class<?> testClass = new TestSuiteID().getTestClass(suiteID);
        JUnitCore.runClasses(testClass);
    }
}
