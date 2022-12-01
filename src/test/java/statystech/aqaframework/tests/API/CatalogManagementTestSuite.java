package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.CatalogManagementSteps;
import statystech.aqaframework.steps.DBsteps.ProductParentSteps;
import statystech.aqaframework.tests.ApiTestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.utils.DataUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class CatalogManagementTestSuite extends ApiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(CatalogManagementTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "26189");
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
                    "\nTest № %d has been started : '%s'\n", testRailID.id(), name));
        }
        LwaTestContext lwaTestContext = new LwaTestContext(name);
        lwaTestContext.getConnectionSandbox();
        Context.addTestContext(lwaTestContext);
    }

    @TestRailID(id = 319229)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void addProductByAPIToProductParent(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(new CatalogManagementSteps().addProduct(
                jsonContent, 200,
                DataUtils.getPropertyValue("tokens.properties", "User24"), lwaTestContext));
        errorMessage.append(new ProductParentSteps().checkProduct(lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    //@TestRailID(id = 319232)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void addProductByAPIToProductTable(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(new CatalogManagementSteps().addProduct(
                jsonContent, 200,
                DataUtils.getPropertyValue("tokens.properties", "User24"), lwaTestContext));
        errorMessage.append(new ProductParentSteps().checkProduct(lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    //@TestRailID(id = 319228)
    //https://statystech.atlassian.net/browse/CM-171
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void validateUserPermissionsToAddProductByAPI(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();
        errorMessage.append(catalogManagementSteps.addProduct(
                jsonContent, 403,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"), lwaTestContext));
        catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "User does not have permission to add a new product. Please contact support at");
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    //@TestRailID(id = 319230)
    //https://statystech.atlassian.net/browse/CM-171
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10UnitsWithoutName.json"})
    public void addProductByAPIMandatoryInformationNotProvided(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();
        errorMessage.append(catalogManagementSteps.addProduct(
                jsonContent, 400,
                DataUtils.getPropertyValue("tokens.properties", "User24"), lwaTestContext));
        catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "The mandatory information is not provided. Please contact support at");
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }
}
