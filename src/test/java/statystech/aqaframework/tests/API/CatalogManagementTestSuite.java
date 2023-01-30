package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.*;
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
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class CatalogManagementTestSuite extends ApiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(CatalogManagementTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
        DBUtils.executeSqlScript("cleanup_productBotox10.sql");
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
    public void addProductByAPIToProductParentTable(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(new CatalogManagementSteps().addProductParent(
                jsonContent, 200,
                DataUtils.getPropertyValue("tokens.properties", "User24"), lwaTestContext));
        errorMessage.append(new ProductParentSteps().checkProduct(lwaTestContext));
        DBUtils.executeSqlScript("cleanup_productBotox10.sql");
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 319232)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void addProductByAPIToProductTable(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        logger.info("-----------------------Precondition-----------------------");
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(new CatalogManagementSteps().addProductParent(
                jsonContent,
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        logger.info("-----------------------Step 1-----------------------");
        errorMessage.append(new CatalogManagementSteps().addProduct(
                lwaTestContext.getProductParentID(),
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        DBUtils.executeSqlScript("cleanup_productBotox10.sql");
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 319228)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void validateUserPermissionsToAddProductParentByAPI(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();
        errorMessage.append(catalogManagementSteps.addProductParent(
                jsonContent, 403,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"), lwaTestContext));
        errorMessage.append(catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "User does not have permission to access the endpoint. Please contact support at"));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 319230)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10UnitsWithoutName.json"})
    public void addProductParentByAPIMandatoryInformationNotProvided(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();
        errorMessage.append(catalogManagementSteps.addProductParent(
                jsonContent, 400,
                DataUtils.getPropertyValue("tokens.properties", "User24"), lwaTestContext));
        errorMessage.append(catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "Invalid request body"));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 327198)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void addProductToProductTableByBMuser(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        logger.info("-----------------------Precondition-----------------------");
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(new CatalogManagementSteps().addProductParent(
                jsonContent,
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        logger.info("-----------------------Step 1-----------------------");
        errorMessage.append(new CatalogManagementSteps().addProduct(
                lwaTestContext.getProductParentID(),
                200,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24"),
                lwaTestContext));
        DBUtils.executeSqlScript("cleanup_productBotox10.sql");
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 319231)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void validateIfUserHasPermissionToAddNewProductVariant(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();
        logger.info("-----------------------Precondition-----------------------");
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(catalogManagementSteps.addProductParent(
                jsonContent,
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        logger.info("-----------------------Step 1-----------------------");
        errorMessage.append(catalogManagementSteps.addProduct(
                lwaTestContext.getProductParentID(),
                403,
                DataUtils.getPropertyValue("tokens.properties", "nonWHMuser"),
                lwaTestContext));
        errorMessage.append(catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "User does not have permission to access the endpoint. Please contact support at"));
        DBUtils.executeSqlScript("cleanup_productBotox10.sql");
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 319233)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void addProductVariantWhichIsNotExistAtProductParentTable(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();
        logger.info("-----------------------Precondition-----------------------");
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(catalogManagementSteps.addProductParent(
                jsonContent,
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        logger.info("-----------------------Step 1-----------------------");
        errorMessage.append(catalogManagementSteps.addProduct(
                100500,
                400,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24"),
                lwaTestContext));
        errorMessage.append(catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "Unknown product. Please contact support at"));
        logger.info("-----------------------Step 2-----------------------");
        errorMessage.append(catalogManagementSteps.addProduct(
                100500,
                400,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        errorMessage.append(catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "Unknown product. Please contact support at"));
        DBUtils.executeSqlScript("cleanup_productBotox10.sql");
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 351027)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void validateProductSearchResults(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();

        logger.info("-----------------------Step 1-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Unitssss",
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertNull(lwaTestContext.getProducts(), "\nResponse is NOT empty, but should be!");

        logger.info("-----------------------Step 2-----------------------");
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(catalogManagementSteps.addProductParent(
                jsonContent,
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertTrue(lwaTestContext.getProducts().get(0).getProductName().equalsIgnoreCase("BOTOX 10 Units"));

        logger.info("-----------------------Step 3-----------------------");
        errorMessage.append(catalogManagementSteps.validateSearchResponseRequiredFields(lwaTestContext));

        logger.info("-----------------------Step 4-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10",
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertTrue(lwaTestContext.getProducts().get(0).getProductName().equalsIgnoreCase("BOTOX 10 Units"));

        logger.info("-----------------------Step 5-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "B",
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertTrue(lwaTestContext.getProducts().get(0).getProductName().equalsIgnoreCase("BOTOX 10 Units"));
        DBUtils.executeSqlScript("cleanup_productBotox10.sql");

        logger.info("-----------------------Step 6-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "",
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));
        assertNull(lwaTestContext.getProducts(), "\nResponse is NOT empty, but should be!");
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 351026)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void validateUserPermissionToSearch(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();
        logger.info("-----------------------Precondition-----------------------");
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(catalogManagementSteps.addProductParent(
                jsonContent,
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));

        logger.info("-----------------------Step 1-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                200,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertTrue(lwaTestContext.getProducts().get(0).getProductName().equalsIgnoreCase("BOTOX 10 Units"));

        logger.info("-----------------------Step 2-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertTrue(lwaTestContext.getProducts().get(0).getProductName().equalsIgnoreCase("BOTOX 10 Units"));


        logger.info("-----------------------Step 3-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                400,
                DataUtils.getPropertyValue("tokens.properties", "WHO"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertNull(lwaTestContext.getProducts(), "\nResponse is NOT empty, but should be!");
        errorMessage.append(catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "User is not authenticated. Please contact support at"));
    }

    @TestRailID(id = 351029)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void validateUserPermissionToPartialSearch(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();
        logger.info("-----------------------Precondition-----------------------");
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(catalogManagementSteps.addProductParent(
                jsonContent,
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));

        logger.info("-----------------------Step 1-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                200,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertEquals(1, lwaTestContext.getProducts().size());
        assertTrue(lwaTestContext.getProducts().get(0).getProductName().equalsIgnoreCase("BOTOX 10 Units"));

        logger.info("-----------------------Step 2-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertEquals(1, lwaTestContext.getProducts().size());
        assertTrue(lwaTestContext.getProducts().get(0).getProductName().equalsIgnoreCase("BOTOX 10 Units"));


        logger.info("-----------------------Step 3-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                400,
                DataUtils.getPropertyValue("tokens.properties", "WHO"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertNull(lwaTestContext.getProducts(), "\nResponse is NOT empty, but should be!");
        errorMessage.append(catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "User is not authenticated. Please contact support at"));
    }

    @TestRailID(id = 351029)
    @ParameterizedTest
    @ValueSource(strings = {"productBotox10Units.json"})
    public void validateUserPermissionToPartialSearch(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        CatalogManagementSteps catalogManagementSteps = new CatalogManagementSteps();
        logger.info("-----------------------Precondition-----------------------");
        String jsonContent = new JsonUtils().getProductsObjectsAndLoadToContext(jsonFilename, lwaTestContext);
        errorMessage.append(catalogManagementSteps.addProductParent(
                jsonContent,
                200,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                lwaTestContext));

        logger.info("-----------------------Step 1-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                200,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertEquals(1, lwaTestContext.getProducts().size());
        assertTrue(lwaTestContext.getProducts().get(0).getProductName().equalsIgnoreCase("BOTOX 10 Units"));

        logger.info("-----------------------Step 2-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                200,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertEquals(1, lwaTestContext.getProducts().size());
        assertTrue(lwaTestContext.getProducts().get(0).getProductName().equalsIgnoreCase("BOTOX 10 Units"));


        logger.info("-----------------------Step 3-----------------------");
        errorMessage.append(catalogManagementSteps.searchProduct(
                "BOTOX 10 Units",
                400,
                DataUtils.getPropertyValue("tokens.properties", "WHO"),
                lwaTestContext));
        assertTrue(errorMessage.toString().isEmpty(), errorMessage.toString());
        assertNull(lwaTestContext.getProducts(), "\nResponse is NOT empty, but should be!");
        errorMessage.append(catalogManagementSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(),
                "User is not authenticated. Please contact support at"));
    }
}
