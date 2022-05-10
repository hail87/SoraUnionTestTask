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
import statystech.aqaframework.TableObjects.ParcelTable;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class AddParcelTestSuite extends TestClass {

    private static final Logger logger = LoggerFactory.getLogger(AddParcelTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "3418");
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

    //https://statystech.atlassian.net/browse/OMS-954
    //https://statystech.atlassian.net/browse/OMS-955
    @TestRailID(id = 17866)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void wrongUserRole(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        logger.info("------------------------------------Precondition Step 4------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostStartFulfillment(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7")));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 5,6------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Step 1------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                testInfo, 403));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        String response = lwaTestContext.getParcelLineResponseBody();

        logger.info("------------------------------------Response------------------------------------\n" + response);

        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(response, "User does not have permission to access the endpoint. Please contact support at"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 17870)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void verifyParcelIdCreatedInParcelTable(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        logger.info("------------------------------------Precondition Step 4------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostStartFulfillment(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7")));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 5,6------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Step 1------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo, 200));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Response------------------------------------\n" + lwaTestContext.getParcelLineResponseBody());

        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponseBody(), String.valueOf(lwaTestContext.getParcelID())));

        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponseBody(), String.valueOf(lwaTestContext.getParcelID())));

        assertTrue(new ParcelTable().checkRowWithIDExist(lwaTestContext.getParcelID()));
        assertEquals(Integer.parseInt(new ParcelTable().getColumnValueByPrimaryID(lwaTestContext.getParcelID(), "warehouseOrderID")), warehouseOrderId);
    }

    @TestRailID(id = 17871)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria2.json"})
    public void validateParcelLineIdAssociatedWithAnyParcel(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        logger.info("------------------------------------Precondition Step 4------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostStartFulfillment(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7")));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 5,6------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        logger.info("------------------------------------Precondition Step 7------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Step 1------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo, 400));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Response------------------------------------\n" + lwaTestContext.getParcelLineResponseBody());

        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponseBody(),
                "One or more parcel lines have assigned to a parcel already. Please contact support at"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

    }

    @TestRailID(id = 17867)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria1.json"})
    public void verifyParcelLinesHasAColdProduct(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 2------------------------------------");

        new ProductSteps().changeProductParentID(3,"DEPO-PROVERA®");
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 4------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        logger.info("------------------------------------Precondition Step 5------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostStartFulfillment(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7")));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 6,7------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Step 1------------------------------------");

        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelsAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo, 400));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Response------------------------------------\n" + lwaTestContext.getParcelLineResponseBody());

        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponseBody(),
                "One or more parcel lines has a cold product and should be in a separate parcel"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 17868)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderProductIsCold6.json"})
    public void verifyIsColdTrue(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new ProductSteps().changeIsCold(1,"SAYPHA® FILLER with Lidocaine");
        new ProductSteps().changeIsCold(1,"SAYPHA® RICH");

        logger.info("------------------------------------Precondition Step 3------------------------------------");
        new OrdersSteps().setOrderIDtoContext();

        logger.info("------------------------------------Precondition Step 4------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());

        logger.info("------------------------------------Precondition Step 5------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostStartFulfillment(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7")));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 6,7------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Step 1------------------------------------");

        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelsAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo, 200));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        ParcelTable parcelTable = new ParcelTable();
        assertTrue(parcelTable.checkRowWithIDExist(lwaTestContext.getParcelID()));
        assertTrue(parcelTable.checkRowWithValueIsPresent("warehouseOrderID", String.valueOf(warehouseOrderId)));
    }

    @TestRailID(id = 17869)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria4.json"})
    public void verifyWarehouseOrderStatusIdNotIn7n10(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        logger.info("------------------------------------Precondition Step 4------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostStartFulfillment(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7")));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 5,6------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 7,8------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo, 200));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 9------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostRequestExternalShipmentAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                "WE2182834TY",
                testInfo));

        logger.info("Response:\n" + lwaTestContext.getParcelLineResponseBody());

        logger.info("------------------------------------Step 1------------------------------------");

        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo, 400));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Response------------------------------------\n" + lwaTestContext.getParcelLineResponseBody());

        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponseBody(),
                String.format("WarehouseOrder with id: %d has not allowed status: 11", warehouseOrderId)));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }
}

//git update-index --no-assume-unchanged settings.xml