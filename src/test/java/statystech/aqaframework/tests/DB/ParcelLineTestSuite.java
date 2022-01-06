package statystech.aqaframework.tests.DB;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.ProductTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.ParcelLineApiSteps;
import statystech.aqaframework.steps.DBsteps.*;
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
public class ParcelLineTestSuite extends TestClass {

    private static final Logger logger = LoggerFactory.getLogger(ParcelLineTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "3340");
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

    @TestRailID(id = 16811)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void updateParcelLineNonExistentWarehouseBatchInventoryID(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        new OrdersSteps().setOrderIDtoContext();

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID()),
                DataUtils.getPropertyValue("tokens.properties", "WHMuser20"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        assertTrue(parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "WHMuser20"), 400, -1, testInfo).isEmpty());
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponse().body().string(), "No value present"));


        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    // wrong test case! needs to be updated at the TestRail
//    @TestRailID(id = 16812)
//    @ParameterizedTest
//    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
//    public void checkParcelLineBatchQuantity(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
//        StringBuilder errorMessage = new StringBuilder();
//        StageOrderSteps stageOrderSteps = new StageOrderSteps();
//        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
//        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
//
//        new OrdersSteps().setOrderIDtoContext();
//        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
//        int warehouseOrderID = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
//
//        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
//        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
//                warehouseOrderID,
//                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
//                testInfo));
//        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
//
//        int productID = new ProductTable().getPrimaryID("productAllSysID", "82290");
//
//        errorMessage.append(parcelLineApiSteps.addNewProductButch(
//                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
//                productID, 1, testInfo));
//        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
//
//        //String productBatchID = new ProductBatchSteps().getProductBatchID(productID); //date to be added
//        errorMessage.append(new WarehouseBatchInventorySteps().getWarehouseBatchInventoryID(lwaTestContext));
//
//        assertTrue(parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
//                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
//                200,
//                lwaTestContext.getWarehouseBatchInventoryID(),
//                testInfo).isEmpty());
//        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponse().body().string(), "Batch does not have enough inventory"));
//
//        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
//    }

    @TestRailID(id = 16813)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void updateParcelLineWrongUserRole(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        new OrdersSteps().setOrderIDtoContext();

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID()),
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        assertTrue(parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19"),
                400,
                lwaTestContext.getWarehouseBatchInventoryID(),
                testInfo).isEmpty());
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponse().body().string(), "Access denied for the user auto_WHM to warehouse: 6"));

        assertTrue(parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "CSRuser-1"),
                400,
                lwaTestContext.getWarehouseBatchInventoryID(),
                testInfo).isEmpty());
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponse().body().string(), "User does not have permission to access the endpoint. Please contact support at xxxxxx@xxx.com"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    //https://statystech.atlassian.net/browse/LWA-1084
    @TestRailID(id = 16869)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void WarehouseBatchInventoryIdFromDifferentWarehouse(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        new OrdersSteps().setOrderIDtoContext();

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID()),
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        assertTrue(parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19"),
                400,
                lwaTestContext.getWarehouseBatchInventoryID(),
                testInfo).isEmpty());
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponse().body().string(), "Access denied for the user auto_WHM to warehouse: 6"));

        assertTrue(parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "CSRuser-1"),
                400,
                lwaTestContext.getWarehouseBatchInventoryID(),
                testInfo).isEmpty());
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponse().body().string(), "Access denied for the user test_csr to warehouse: 6"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 16810)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void updateParcelLineParcelStatusIsC(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        logger.info("------------------------------------Precondition Step 4------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostStartFulfillment(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7")));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 5------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 6,7------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 8------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostRequestExternalShipmentAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));

        logger.info("------------------------------------Step 1------------------------------------");
        parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                400,
                lwaTestContext.getWarehouseBatchInventoryID(),
                testInfo);
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponse().body().string(), "The parcel has a complete status"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

}
