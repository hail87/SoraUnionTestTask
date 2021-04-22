package statystech.aqaframework.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import statystech.aqaframework.tests.TestRail.TestSuiteID;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.util.Properties;

public class TestRunner {

    @Test
    public void testRunner() throws IOException {

        String runID = System.getProperty("runID");
        String suiteID = System.getProperty("suiteID");
        System.out.println("runID: " + runID);
        System.out.println("suiteID: " + suiteID);
        if (runID != null & suiteID != null) {
            DataUtils.updateTestRailPropertyParameter("testrail_runId", runID);
            DataUtils.updateTestRailPropertyParameter("testrail_testSuiteId", suiteID);
        } else {
            Properties properties = DataUtils.getProperty("test_rail_config.properties");
            suiteID = properties.getProperty("testrail_testSuiteId").trim();
        }
        Class<?> testClass = new TestSuiteID().getTestClass(suiteID);
        JUnitCore.runClasses(testClass);
    }
}
