package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.OrderLineApiSteps;
import statystech.aqaframework.steps.DBsteps.OrderLineSteps;
import statystech.aqaframework.steps.DBsteps.OrdersSteps;
import statystech.aqaframework.steps.DBsteps.StageOrderSteps;
import statystech.aqaframework.steps.DBsteps.WarehouseOrderSteps;
import statystech.aqaframework.tests.ApiTestClass;
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
public class CancelWarehouseOrderLineTestSuite extends ApiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(CancelWarehouseOrderLineTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "13442");
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

    @TestRailID(id = 165722)
    @ParameterizedTest
    @ValueSource(strings = {"CancelWHOrder-C165722.json"})
    public void validateQuantityValueIsNotValid(String jsonFilename, TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        new OrderLineSteps().setOrderLineIDtoContext(warehouseOrderId, lwaTestContext);

        logger.info("------------------------------------Step 1------------------------------------");
        OrderLineApiSteps orderLineApiSteps = new OrderLineApiSteps();
        errorMessage.append(orderLineApiSteps.sendPutRequestAndSaveResponseToContext(
                400,
                4,
                lwaTestContext,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24")));
        errorMessage.append(orderLineApiSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(), "The cancelled quantity is missed or incorrect. Please contact support at"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 165624)
    @ParameterizedTest
    @ValueSource(strings = {"CancelWHOrder-C165624.json"})
    public void validateUserRole(String jsonFilename, TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        new OrderLineSteps().setOrderLineIDtoContext(warehouseOrderId, lwaTestContext);

        logger.info("------------------------------------Step 1------------------------------------");
        OrderLineApiSteps orderLineApiSteps = new OrderLineApiSteps();
        errorMessage.append(orderLineApiSteps.sendPutRequestAndSaveResponseToContext(
                403,
                2,
                lwaTestContext,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm")));
        errorMessage.append(orderLineApiSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(), "User does not have permission to access the endpoint. Please contact support at"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        logger.info("------------------------------------Step 2------------------------------------");
        errorMessage.append(orderLineApiSteps.sendPutRequestAndSaveResponseToContext(
                403,
                2,
                lwaTestContext,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser20")));
        errorMessage.append(orderLineApiSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(), "User does not have permission to access the endpoint. Please contact support at"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 165625)
    @ParameterizedTest
    @ValueSource(strings = {"CancelWHOrder-C165625.json"})
    public void validateOrderLineIdNotValid(String jsonFilename, TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        new OrderLineSteps().setOrderLineIDtoContext(warehouseOrderId, lwaTestContext);

        logger.info("------------------------------------Step 1------------------------------------");
        OrderLineApiSteps orderLineApiSteps = new OrderLineApiSteps();
        errorMessage.append(orderLineApiSteps.sendPutRequestAndSaveResponseToContext(
                400,
                2,
                -31540,
                lwaTestContext,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24")));
        errorMessage.append(orderLineApiSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(), "Order Line ID: -31540 does not exist. Please contact support at"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 165644)
    @ParameterizedTest
    @ValueSource(strings = {"CancelWHOrder-C165644.json"})
    public void validateOrderStatusNotValid(String jsonFilename, TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        WarehouseOrderSteps warehouseOrderSteps = new WarehouseOrderSteps();
        int warehouseOrderId = warehouseOrderSteps.getWarehouseOrderId(lwaTestContext.getOrderID());
        new OrderLineSteps().setOrderLineIDtoContext(warehouseOrderId, lwaTestContext);
        logger.info("------------------------------------Precondition Step 4------------------------------------");
        warehouseOrderSteps.getWarehouseOrderTable().updateProperRow("warehouseOrderStatusID", "11", "warehouseOrderID", String.valueOf(warehouseOrderId));

        logger.info("------------------------------------Step 1------------------------------------");
        OrderLineApiSteps orderLineApiSteps = new OrderLineApiSteps();
        errorMessage.append(orderLineApiSteps.sendPutRequestAndSaveResponseToContext(
                400,
                2,
                lwaTestContext,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24")));
        errorMessage.append(orderLineApiSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(), "The warehouse order is in invalid status. Please contact support at"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 170490)
    @ParameterizedTest
    @ValueSource(strings = {"CancelWHOrder-C170490.json"})
    public void validateOrderFromLwaWasNotProcessedByOms(String jsonFilename, TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        WarehouseOrderSteps warehouseOrderSteps = new WarehouseOrderSteps();
        int warehouseOrderId = warehouseOrderSteps.getWarehouseOrderId(lwaTestContext.getOrderID());
        new OrderLineSteps().setOrderLineIDtoContext(warehouseOrderId, lwaTestContext);

        logger.info("------------------------------------Step 1------------------------------------");
        OrderLineApiSteps orderLineApiSteps = new OrderLineApiSteps();
        errorMessage.append(orderLineApiSteps.sendPutRequestAndSaveResponseToContext(
                400,
                2,
                lwaTestContext,
                DataUtils.getPropertyValue("tokens.properties", "BM_user_24")));
        errorMessage.append(orderLineApiSteps.verifyActualResultsContains(lwaTestContext.getResponseBody(), "This is not an OMS order. Please contact support at"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }
}
