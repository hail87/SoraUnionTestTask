package statystech.aqaframework.tests.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.json.StringEscapeUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OrderJackson.OrderItem;
import statystech.aqaframework.DataObjects.ProductJson.BatchesItem;
import statystech.aqaframework.DataObjects.ProductJson.ItemsItem;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.TableObjects.ParcelTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.enums.GetWarehouseOrderNoCriteriaEnum;
import statystech.aqaframework.steps.APIsteps.LwaApiSteps;
import statystech.aqaframework.steps.APIsteps.ParcelLineApiSteps;
import statystech.aqaframework.steps.DBsteps.*;
import statystech.aqaframework.tests.ApiTestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.DataUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class ApiSmokeTestSuite extends ApiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(ApiSmokeTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "9014");
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

    @TestRailID(id = 100980)
    @ParameterizedTest
    @ValueSource(strings = {"order1000100data.json"})
    public void newOrderProcessing(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToTableAndLwaContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        OrdersSteps ordersSteps = new OrdersSteps();
        ordersSteps.setOrderIDtoContext();
        errorMessage.append(ordersSteps.checkOrdersTable());
        errorMessage.append(new UserTableSteps().checkAllSysUserIDColumn());
        errorMessage.append(new ShippingAddressSteps().checkShippingAddressTable());
        errorMessage.append(new BuyerSteps().checkBuyerBillingInformation());
        errorMessage.append(new ShopperGroupSteps().checkShopperGroupTable());
        for (OrderItem item : getLwaTestContext(testInfo).getOrder().getOrderItems()) {
            errorMessage.append(new ProductSteps().checkProduct(item));
            errorMessage.append(new ProductBatchSteps().checkBatchNumber(item));
            errorMessage.append(new OrderLineSteps().checkOrderLineTableAndSetWarehouseOrderID(item));
        }
        errorMessage.append(new WarehouseOrderSteps().checkWarehouseOrderTable());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100981)
    @ParameterizedTest
    @CsvSource({"Order4190168data.json, Order4190168dataUpdate.json"})
    public void orderUpdateAddProduct(String newOrderJson, String updateOrderJson, TestInfo testInfo) throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int idNew = stageOrderSteps.insertJsonToTableAndLwaContext(newOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
        OrderLineSteps orderLineSteps = new OrderLineSteps();
        new OrdersSteps().setOrderIDtoContext();
        OrderItem product1 = getLwaTestContext(testInfo).getItem("REVOFIL AQUASHINE BTX");
        errorMessage.append(orderLineSteps.checkOrderLineTableAndSetWarehouseOrderID(product1));

        errorMessage.append(orderLineSteps.checkProductIsAbsent(StringEscapeUtils.unescapeJava("EYLEA\\u00ae 40mg/1ml Non-English")));
        int idUpdate = stageOrderSteps.insertJsonToTableAndLwaContext(updateOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());

        OrderItem product2 = getLwaTestContext(testInfo).getItem(StringEscapeUtils.unescapeJava("EYLEA\\u00ae 40mg/1ml Non-English"));
        new WarehouseOrderSteps().setWarehouseOrders();
        errorMessage.append(orderLineSteps.checkOrderLineTableWithWarehouseOrderID(product2));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        stageOrderSteps.deleteRow(idNew);
        stageOrderSteps.deleteRow(idUpdate);
    }

    @TestRailID(id = 100982)
    @ParameterizedTest
    @CsvSource({"Order9990002data.json,  Order9990002dataUpdate.json"})
    public void orderUpdateProductRemoved(String newOrderJson, String updateOrderJson, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int idNew = stageOrderSteps.insertJsonToTableAndLwaContext(newOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());

        new OrdersSteps().setOrderIDtoContext();
        WarehouseOrderSteps warehouseOrderSteps = new WarehouseOrderSteps();
        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderQuantity(2));
        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderTable());

        int idUpdate = stageOrderSteps.insertJsonToTableAndLwaContext(updateOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());
        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderIsNotActive("Bulgarium"));

        stageOrderSteps.deleteRow(idNew);
        stageOrderSteps.deleteRow(idUpdate);

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100983)
    @ParameterizedTest
    @CsvSource({"Order1081869.json, Order1081869Cancel.json"})
    public void cancelOrder(String newOrderJson, String updateOrderJson, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int idNew = stageOrderSteps.insertJsonToTableAndLwaContext(newOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());

        OrdersSteps ordersSteps = new OrdersSteps();
        ordersSteps.setOrderIDtoContext();
        errorMessage.append(ordersSteps.checkOrdersTable());

        WarehouseOrderSteps warehouseOrderSteps = new WarehouseOrderSteps();
        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderStatusesIsActive());

        int idUpdate = stageOrderSteps.insertJsonToTableAndLwaContext(updateOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());

        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderIsNotActive("Nickel-28-Ni"));
        errorMessage.append(ordersSteps.checkOrderIsCancelled());

        stageOrderSteps.deleteRow(idNew);
        stageOrderSteps.deleteRow(idUpdate);

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100984)
    @ParameterizedTest
    @CsvSource({"p2.json"})
    public void addProductTest(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageProductSteps stageProductSteps = new StageProductSteps();
        int id = stageProductSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageProductSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        new JsonUtils().getProductJsonObjectAndLoadToContext(jsonFilename, testInfo.getTestMethod().get().getName());

        for (ItemsItem item : getLwaTestContext(testInfo).getProduct().getItems()) {
            errorMessage.append(new ProductSteps().checkProduct(item));
            for (BatchesItem batch : item.getBatches())
                errorMessage.append(new ProductBatchSteps().checkBatchNumberIsPresent(batch));
            errorMessage.append(new ProductDescriptionSteps().checkProductDescription(item));
            errorMessage.append(new WarehouseInventorySteps().checkWarehouseInventory(item));
        }
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100985)
    @ParameterizedTest
    @CsvSource({"ProductsSmallSingleN.json, ProductsSmallUpdateSingle.json"})
    public void updateProduct(String productJson, String updateProductJson, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageProductSteps stageProductSteps = new StageProductSteps();

        int id = stageProductSteps.insertJsonToTableAndContext(productJson, testInfo);
        assertTrue(stageProductSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        id = stageProductSteps.insertJsonToTableAndContext(updateProductJson, testInfo);
        assertTrue(stageProductSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        new JsonUtils().getProductJsonObjectAndLoadToContext(updateProductJson, testInfo.getTestMethod().get().getName());

        ProductBatchSteps productBatchSteps = new ProductBatchSteps();
        ItemsItem item = getLwaTestContext(testInfo).getProduct().getItems().get(0);
        if (item.getJsonNodeBatches() != null) {
            item.evaluateBatch(new ObjectMapper());
            errorMessage.append(productBatchSteps.setProductBatchID(item));
        }
        errorMessage.append(productBatchSteps.checkProductBatchIsPresent("994840"));
        errorMessage.append(productBatchSteps.checkProductBatchIsPresent("995582"));
        errorMessage.append(new ProductSteps().checkProductUnavailable(item));
        errorMessage.append(new WarehouseBatchInventorySteps().checkFreeStock(item));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100986)
    @Test
    public void getWarehouseOrdersNoCriteria(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        StageOrderSteps stageOrderSteps = new StageOrderSteps();

        for (GetWarehouseOrderNoCriteriaEnum json : GetWarehouseOrderNoCriteriaEnum.values()) {
            int idNew = stageOrderSteps.insertJsonToTableAndLwaContext(json.getTitle(), testInfo);
            assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
        }

        ArrayList<Integer> expectedOrderNumbersList = new ArrayList<>(Arrays.asList(6097147, 6097800, 6095793, 6098207, 6097621));
        OrdersTable ordersTable = new OrdersTable();
        for (int orderAllSysID : expectedOrderNumbersList) {
            assertTrue(ordersTable.checkRowWithValueIsPresent("orderAllSysID", String.valueOf(orderAllSysID)));
        }

        LwaApiSteps lwaApiSteps = new LwaApiSteps();

        errorMessage.append(lwaApiSteps.updateLwaContextWithWarehouseSearchResult(ApiRestUtils.getWarehouseOrders(), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(lwaApiSteps.checkWarehouseSearchResponse(
                new ArrayList<>(Arrays.asList(6097147, 6097800, 6095793, 6098207)), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100987)
    @Test
    public void getWarehouseOrdersAllsysOrderId(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        StageOrderSteps stageOrderSteps = new StageOrderSteps();

        for (GetWarehouseOrderNoCriteriaEnum json : GetWarehouseOrderNoCriteriaEnum.values()) {
            int idNew = stageOrderSteps.insertJsonToTableAndLwaContext(json.getTitle(), testInfo);
            assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
        }

        ArrayList<Integer> expectedOrderNumbersList = new ArrayList<>(Arrays.asList(6097147, 6097800, 6095793, 6098207, 6097621));
        OrdersTable ordersTable = new OrdersTable();
        for (int orderAllSysID : expectedOrderNumbersList) {
            assertTrue(ordersTable.checkRowWithValueIsPresent("orderAllSysID", String.valueOf(orderAllSysID)));
        }

        LwaApiSteps lwaApiSteps = new LwaApiSteps();

        errorMessage.append(lwaApiSteps.updateLwaContextWithWarehouseSearchResult(ApiRestUtils.getWarehouseOrders(6097147), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(lwaApiSteps.checkWarehouseSearchResponse(
                new ArrayList<>(Collections.singletonList(6097147)), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100988)
    @Test
    public void getWarehouseOrdersAllsysOrderIdOtherCriteria(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        StageOrderSteps stageOrderSteps = new StageOrderSteps();

        for (GetWarehouseOrderNoCriteriaEnum json : GetWarehouseOrderNoCriteriaEnum.values()) {
            int idNew = stageOrderSteps.insertJsonToTableAndLwaContext(json.getTitle(), testInfo);
            assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
        }

        ArrayList<Integer> expectedOrderNumbersList = new ArrayList<>(Arrays.asList(6097147, 6097800, 6095793, 6098207, 6097621));
        OrdersTable ordersTable = new OrdersTable();
        for (int orderAllSysID : expectedOrderNumbersList) {
            assertTrue(ordersTable.checkRowWithValueIsPresent("orderAllSysID", String.valueOf(orderAllSysID)));
        }

        LwaApiSteps lwaApiSteps = new LwaApiSteps();

        errorMessage.append(lwaApiSteps.updateLwaContextWithWarehouseSearchResult(ApiRestUtils.getWarehouseOrders("EU", "ROTW"), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(lwaApiSteps.checkWarehouseSearchResponse(
                new ArrayList<>(Collections.singletonList(6097621)), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100989)
    @Test
    public void getWarehouseOrdersWrongUserRole() {
        StringBuilder errorMessage = new StringBuilder();
        LwaApiSteps lwaApiSteps = new LwaApiSteps();

        errorMessage.append(lwaApiSteps.verifyExpectedResultsContains(
                ApiRestUtils.getWarehouseOrdersNonWHMuser(),
                "User does not have permission to access the endpoint"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100991)
    @Test
    public void getWarehouseOrdersByWarehouseId(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        StageOrderSteps stageOrderSteps = new StageOrderSteps();

        for (GetWarehouseOrderNoCriteriaEnum json : GetWarehouseOrderNoCriteriaEnum.values()) {
            int idNew = stageOrderSteps.insertJsonToTableAndLwaContext(json.getTitle(), testInfo);
            assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
        }

        ArrayList<Integer> expectedOrderNumbersList = new ArrayList<>(Arrays.asList(6097147, 6097800, 6095793, 6098207, 6097621));
        OrdersTable ordersTable = new OrdersTable();
        for (int orderAllSysID : expectedOrderNumbersList) {
            assertTrue(ordersTable.checkRowWithValueIsPresent("orderAllSysID", String.valueOf(orderAllSysID)));
        }

        LwaApiSteps lwaApiSteps = new LwaApiSteps();

        errorMessage.append(lwaApiSteps.updateLwaContextWithWarehouseSearchResult(ApiRestUtils.getWarehouseOrdersByWarehouseID(24), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(lwaApiSteps.checkWarehouseSearchResponse(
                new ArrayList<>(Arrays.asList(6097621, 6095793)), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100990)
    @Test
    public void getWarehouseOrdersByDate(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        StageOrderSteps stageOrderSteps = new StageOrderSteps();

        for (GetWarehouseOrderNoCriteriaEnum json : GetWarehouseOrderNoCriteriaEnum.values()) {
            int idNew = stageOrderSteps.insertJsonToTableAndLwaContext(json.getTitle(), testInfo);
            assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
        }

        ArrayList<Integer> expectedOrderNumbersList = new ArrayList<>(Arrays.asList(6097147, 6097800, 6095793, 6098207, 6097621));
        OrdersTable ordersTable = new OrdersTable();
        for (int orderAllSysID : expectedOrderNumbersList) {
            assertTrue(ordersTable.checkRowWithValueIsPresent("orderAllSysID", String.valueOf(orderAllSysID)));
        }

        LwaApiSteps lwaApiSteps = new LwaApiSteps();

        errorMessage.append(lwaApiSteps.updateLwaContextWithWarehouseSearchResult(ApiRestUtils.getWarehouseOrdersByDate("2021-10-01", "2021-10-31"), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(lwaApiSteps.checkWarehouseSearchResponse(
                new ArrayList<>(Collections.singletonList(6097147)), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100992)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void updateParcelLineParcelStatusIsC(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndLwaContext(jsonFilename, testInfo);
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

        logger.info("------------------------------------Precondition Step 6,7,8------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 9------------------------------------");
//        errorMessage.append(parcelLineApiSteps.sendPostRequestExternalShipmentAndSaveResponseToContext(
//                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
//                "AA2182814AA",
//                testInfo));
        assertTrue(new ParcelSteps().setParcelStatus("C", lwaTestContext).isEmpty());

        logger.info("------------------------------------Step 1------------------------------------");
        parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                400,
                lwaTestContext.getWarehouseBatchInventoryID(),
                testInfo);
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponseBody(), "The parcel has a complete status"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100993)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void updateParcelLineNonExistentWarehouseBatchInventoryID(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToTableAndLwaContext(jsonFilename, testInfo);
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
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponseBody(), "No value present"));


        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100994)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void updateParcelLineWrongUserRole(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToTableAndLwaContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        new OrdersSteps().setOrderIDtoContext();

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        logger.info("sendGetRequestAndSaveResponseToContext");
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID()),
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("sendPutRequestAndSaveResponseToContext");
        assertTrue(parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "WHMuser19_Csr_Csm"),
                403,
                lwaTestContext.getWarehouseBatchInventoryID(),
                testInfo).isEmpty());
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponseBody(), "Access denied for the user auto_WHM to warehouse: 6"));

        logger.info("sendPutRequestAndSaveResponseToContext");
        assertTrue(parcelLineApiSteps.sendPutRequestAndSaveResponseToContext(
                DataUtils.getPropertyValue("tokens.properties", "CSRuser-1"),
                403,
                lwaTestContext.getWarehouseBatchInventoryID(),
                testInfo).isEmpty());
        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(lwaTestContext.getParcelLineResponseBody(), "User does not have permission to access the endpoint. Please contact support at xxxxxx@xxx.com"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 100995)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void wrongUserRole(String jsonFilename, TestInfo testInfo) throws IOException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndLwaContext(jsonFilename, testInfo);
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

    @TestRailID(id = 100996)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria4.json"})
    public void verifyWarehouseOrderStatusIdNotIn7n10(String jsonFilename, TestInfo testInfo) throws IOException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndLwaContext(jsonFilename, testInfo);
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

    @TestRailID(id = 100997)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void verifyParcelIdCreatedInParcelTable(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndLwaContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        logger.info("------------------------------------Precondition Step 4------------------------------------");
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
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

    @TestRailID(id = 100998)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria2.json"})
    public void validateParcelLineIdAssociatedWithAnyParcel(String jsonFilename, TestInfo testInfo) throws IOException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndLwaContext(jsonFilename, testInfo);
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
}