package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.ResellerPortalSteps;
import statystech.aqaframework.tests.ApiTestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class ResellerPortalTestSuite extends ApiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(ResellerPortalTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "31655");
        DataUtils.saveTestRailProperty(properties);
        if (TestRailReportExtension.isTestRailAnnotationPresent) {
            TestRailReportExtension.reportResults();
        }
    }

    @BeforeEach
    public void setTestContext(TestInfo testInfo) throws SQLException, IOException {
        String name = testInfo.getTestMethod().get().getName();
        TestRailID testRailID = testInfo.getTestMethod().get().getAnnotation(TestRailID.class);
        if (testRailID != null) {
            logger.info(String.format(
                    "\n\n\n\nTest № %d has been started : '%s'\n", testRailID.id(), name));
        }
        LwaTestContext lwaTestContext = new LwaTestContext(name);
        lwaTestContext.getConnectionSandbox();
        Context.addTestContext(lwaTestContext);
    }

    @TestRailID(id = 367883)
    @Test
    public void getListOfResellers(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ResellerPortalSteps resellerPortalSteps = new ResellerPortalSteps();
        errorMessage.append(resellerPortalSteps.getResellersList(
                200,
                DataUtils.getPropertyValue("tokens.properties", "ACC"), lwaTestContext));
        assertEquals(2,lwaTestContext.getResellersListResponse().length);
        errorMessage.append(resellerPortalSteps.getResellersList(
                200,
                DataUtils.getPropertyValue("tokens.properties", "CSH"), lwaTestContext));
        assertEquals(2,lwaTestContext.getResellersListResponse().length);
        errorMessage.append(resellerPortalSteps.getResellersList(
                200,
                DataUtils.getPropertyValue("tokens.properties", "ACM"), lwaTestContext));
        assertEquals(2,lwaTestContext.getResellersListResponse().length);
        errorMessage.append(resellerPortalSteps.getResellersList(
                403,
                DataUtils.getPropertyValue("tokens.properties", "CSR"), lwaTestContext));
        errorMessage.append(resellerPortalSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "User does not have permission to access the endpoint. Please contact support at"));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 367884)
    @Test
    public void getResellersInformation(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ResellerPortalSteps resellerPortalSteps = new ResellerPortalSteps();
        errorMessage.append(resellerPortalSteps.getResellerInformation(
                3,
                200,
                DataUtils.getPropertyValue("tokens.properties", "ACM"), lwaTestContext));
        errorMessage.append(resellerPortalSteps.validateGetResellersInformationRequiredFields(lwaTestContext));
        errorMessage.append(resellerPortalSteps.getResellerInformation(
                3,
                200,
                DataUtils.getPropertyValue("tokens.properties", "RES"), lwaTestContext));
        errorMessage.append(resellerPortalSteps.validateGetResellersInformationRequiredFields(lwaTestContext));
        errorMessage.append(resellerPortalSteps.getResellerInformation(
                3,
                200,
                DataUtils.getPropertyValue("tokens.properties", "ACC"), lwaTestContext));
        errorMessage.append(resellerPortalSteps.validateGetResellersInformationRequiredFields(lwaTestContext));
        errorMessage.append(new ResellerPortalSteps().getResellerInformation(
                3,
                403,
                DataUtils.getPropertyValue("tokens.properties", "CSMuser23"), lwaTestContext));
        errorMessage.append(resellerPortalSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "User does not have permission to get reseller general information. Please"));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        errorMessage.append(new ResellerPortalSteps().getResellerInformation(
                3,
                403,
                DataUtils.getPropertyValue("tokens.properties", "CSH"), lwaTestContext));
        errorMessage.append(resellerPortalSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "User does not have permission to get reseller general information. Please contact support at"));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }
}
