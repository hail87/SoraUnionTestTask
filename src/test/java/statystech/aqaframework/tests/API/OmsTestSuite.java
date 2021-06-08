package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.OmsTestContext;
import statystech.aqaframework.steps.APIsteps.OmsApiSteps;
import statystech.aqaframework.tests.TestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class OmsTestSuite extends TestClass {

    @BeforeEach
    public void setTestContext(TestInfo testInfo) throws SQLException, IOException {
        OmsTestContext omsTestContext = new OmsTestContext(testInfo.getTestMethod().get().getName());
        omsTestContext.getConnection();
        Context.addTestContext(omsTestContext);
    }

    @TestRailID(id = 7743)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-newBuyer.json"})
    public void submitOrderNoBuyerAccountId(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @BeforeAll
    static void clearTestResults(){
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID(){
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "2170");
        DataUtils.saveTestRailProperty(properties);
        if (TestRailReportExtension.isTestRailAnnotationPresent) {
            TestRailReportExtension.reportResults();
        }
    }
}
