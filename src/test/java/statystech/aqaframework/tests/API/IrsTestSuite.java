package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.IrsApiSteps;
import statystech.aqaframework.tests.ApiTestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class IrsTestSuite extends ApiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(IrsTestSuite.class);

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
        lwaTestContext.getConnectionSandbox();
        Context.addTestContext(lwaTestContext);
    }


    //https://statystech.atlassian.net/browse/OMS-930
    @TestRailID(id = 95187)
    @Test
    public void wrongUserRole(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostProductSearchAndSaveResponseToContext(
                "RESTYLANE™",
                403,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24"),
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
                DataUtils.getPropertyValue("tokens.properties", "CSMuser23"),
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
                DataUtils.getPropertyValue("tokens.properties", "WHMuser20"),
                lwaTestContext));
        assertTrue(lwaTestContext.getResponseBody().contains("User does not have permission to use API method."));
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
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext));

        assertTrue(irsApiSteps.verifyGetProductDetailsResponse(2392, lwaTestContext).isEmpty());
    }

    @TestRailID(id = 126273)
    @Test
    public void verifyPartialProductSearchResults(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();

        errorMessage.append(irsApiSteps.sendPostPartialProductSearchAndSaveResponseToContext(
                "R",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext));
        errorMessage.append(irsApiSteps.verifyAllProductsAtTheSearchResponseContainsString("R", lwaTestContext));

        errorMessage.append(irsApiSteps.sendPostPartialProductSearchAndSaveResponseToContext(
                "RE",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext));
        errorMessage.append(irsApiSteps.verifyAllProductsAtTheSearchResponseContainsString("RE", lwaTestContext));

        errorMessage.append(irsApiSteps.sendPostPartialProductSearchAndSaveResponseToContext(
                "REF",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext));
        errorMessage.append(irsApiSteps.verifyAllProductsAtTheSearchResponseContainsString("REF", lwaTestContext));

        errorMessage.append(irsApiSteps.sendPostPartialProductSearchAndSaveResponseToContext(
                "REF",
                "2391",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext));
        errorMessage.append(irsApiSteps.verifyAllProductsAtTheSearchResponseContainsString("REF", lwaTestContext));
        errorMessage.append(irsApiSteps.verifyResultNotContain("2391", lwaTestContext));

        errorMessage.append(irsApiSteps.sendPostPartialProductSearchAndSaveResponseToContext(
                "PLASMOLIFTING",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext));
        errorMessage.append(irsApiSteps.verifySearchResponseProductEmpty(lwaTestContext));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 130882)
    @Test
    public void addProductBatchVerifyUserRole(TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostAddNewProductBatchAndSaveResponseToContext(
                2391,
                10,
                403,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24"),
                lwaTestContext
        ));
        assertTrue(lwaTestContext.getResponseBody().contains("{\"message_user\":\"User does not have permission to access the endpoint. Please contact support at "));
    }

    @TestRailID(id = 130947)
    @Test
    public void addProductBatchVerifyWrongProductId(TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostAddNewProductBatchAndSaveResponseToContext(
                -1000,
                10,
                400,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext
        ));
        assertTrue(lwaTestContext.getResponseBody().contains("{\"message_user\":\"Unknown product ID. Please contact support at"));
    }

    @TestRailID(id = 130969)
    @Test
    public void addProductBatchVerifyWrongWarehouseId(TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostAddNewProductBatchAndSaveResponseToContext(
                2391,
                -1000,
                400,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext
        ));
        assertTrue(lwaTestContext.getResponseBody().contains("{\"message_user\":\"Unknown warehouse ID. Please contact support at"));
    }

    @TestRailID(id = 131399)
    @Test
    public void addProductBatchVerifyBatchAlreadyExist(TestInfo testInfo) throws SQLException, IOException {
        DBUtils.cleanDB("clean_all_lwa_test_data.sql");
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostAddNewProductBatchAndSaveResponseToContext(
                2391,
                10,
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext
        ));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(irsApiSteps.sendPostAddNewProductBatchAndSaveResponseToContext(
                2391,
                10,
                400,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                lwaTestContext
        ));
        assertTrue(lwaTestContext.getResponseBody().contains("{\"message_user\":\"The batch number already exists. Please update the existing batch number or contact support at"));
    }

    @TestRailID(id = 132369)
    @Test
    public void addProductBatchVerifyFutureDate(TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        IrsApiSteps irsApiSteps = new IrsApiSteps();
        errorMessage.append(irsApiSteps.sendPostAddNewProductBatchAndSaveResponseToContext(
                2391,
                10,
                400,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                "2020-07-31",
                lwaTestContext
        ));
        if (!lwaTestContext.getResponseBody().contains("{\"message_user\":\"The expiry date should be in the future. Please contact support")) {
            fail("\naddProductBatch with date in the past was passed successfully, but shouldn't\n");
        }
            errorMessage.append(irsApiSteps.sendPostAddNewProductBatchAndSaveResponseToContext(
                    2391,
                    10,
                    200,
                    DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                    DataUtils.getCurrentDate(),
                    lwaTestContext
            ));
        assertTrue(lwaTestContext.getResponseBody().contains("product_batch_id"));
    }
}
