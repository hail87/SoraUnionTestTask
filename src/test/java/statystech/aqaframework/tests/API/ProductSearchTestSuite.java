package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.IrsApiSteps;
import statystech.aqaframework.steps.APIsteps.OmsApiSteps;
import statystech.aqaframework.steps.DBsteps.OrdersSteps;
import statystech.aqaframework.tests.TestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class ProductSearchTestSuite extends TestClass {

    private static final Logger logger = LoggerFactory.getLogger(ProductSearchTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "1960");
        DataUtils.saveTestRailProperty(properties);
        if (TestRailReportExtension.isTestRailAnnotationPresent) {
            TestRailReportExtension.reportResults();
        }
    }

    @BeforeEach
    public void setTestContext(TestInfo testInfo) throws SQLException, IOException {
        String name = testInfo.getTestMethod().get().getName();
        logger.info(String.format(
                "\nTest № %d has been started : '%s'\n", testInfo.getTestMethod().get().getAnnotation(TestRailID.class).id(), name));
        LwaTestContext lwaTestContext = new LwaTestContext(name);
        lwaTestContext.getConnection();
        Context.addTestContext(lwaTestContext);
    }


    @TestRailID(id = 95187)
    @Test
    public void wrongUserRole(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostProductSearchAndSaveResponseToContext(
                "RESTYLANE™",
                403,
                DataUtils.getPropertyValue("tokens.properties", "ProductSearchWrongUserRole"),
                lwaTestContext));
        assertTrue(lwaTestContext.getResponseBody().contains("User does not have permission to access the endpoint."));
    }

    @TestRailID(id = 95820)
    @Test
    public void verifyProductUnavailable1(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostProductSearchAndSaveResponseToContext(
                "PLASMOLIFTING™ CENTRIFUGE XC 2000",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                lwaTestContext));
        errorMessage.append(irsApiSteps.verifySearchResponseProductUnavailable(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 95821)
    @Test
    public void verifyIfMethodReturnResultBasedOnPartialSearch(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostProductSearchAndSaveResponseToContext(
                "REST",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                lwaTestContext));
        errorMessage.append(irsApiSteps.verifyAllProductsAtTheSearchResponseContainsString("REST", lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 95822)
    @Test
    public void verifyIfMethodReturnCorrectResult(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostProductSearchAndSaveResponseToContext(
                "REST® LIP REFRESH™",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                lwaTestContext));
        assertEquals(lwaTestContext.getSearchProductResponse().getProductRecords().get(0).getProductName(), "REST® LIP REFRESH™");
        errorMessage.append(irsApiSteps.verifySearchResponse(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 96313)
    @Test
    public void verifyIfWebsiteIdIsProvided(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostProductSearchAndSaveResponseToContext(
                "REST® LIP REFRESH™",
                400,
                DataUtils.getPropertyValue("tokens.properties", "CSRuser-1"),
                lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        assertTrue(lwaTestContext.getResponseBody().contains("Website is required. Please contact support at"));

        errorMessage.append(irsApiSteps.sendPostProductSearchAndSaveResponseToContext(
                "REST® LIP REFRESH™",
                400,
                DataUtils.getPropertyValue("tokens.properties", "CSMuser"),
                lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        assertTrue(lwaTestContext.getResponseBody().contains("Website is required. Please contact support at"));

    }

    @TestRailID(id = 124928)
    @Test
    public void verifyUserRole(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendGetProductDetailsAndSaveResponseToContext(
                2392,
                403,
                DataUtils.getPropertyValue("tokens.properties", "ProductSearchWrongUserRole"),
                lwaTestContext));
        assertTrue(lwaTestContext.getResponseBody().contains("User does not have permission to access the endpoint."));
    }

    @TestRailID(id = 124929)
    @Test
    public void verifyProductDetailsResults(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendGetProductDetailsAndSaveResponseToContext(
                2392,
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19"),
                lwaTestContext));

        assertTrue(irsApiSteps.verifyGetProductDetailsResponse(2392,lwaTestContext).isEmpty());
    }

//    @TestRailID(id = 96314)
//    @Test
//    public void verifyWebsiteIdExist(TestInfo testInfo) throws IOException {
//        StringBuilder errorMessage = new StringBuilder();
//        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
//    }
//
//    @TestRailID(id = 96315)
//    @Test
//    public void verifyForWebsiteIdIfTheUserHasPermission(TestInfo testInfo) throws IOException {
//        StringBuilder errorMessage = new StringBuilder();
//        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
//    }
}
