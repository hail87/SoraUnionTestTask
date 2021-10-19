package statystech.aqaframework.tests.DB;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
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
import statystech.aqaframework.enums.GetWarehouseOrderNoCriteriaEnum;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.LwaApiSteps;
import statystech.aqaframework.steps.DBsteps.*;
import statystech.aqaframework.tests.TestClass;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.tests.TestRail.TestRailID;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class LwaTestSuite extends TestClass {

    private static final Logger logger = LoggerFactory.getLogger(LwaTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "1");
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

    @TestRailID(id = 1)
    @ParameterizedTest
    @ValueSource(strings = {"order1000100data.json"})
    public void newOrderProcessing(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        OrdersSteps ordersSteps = new OrdersSteps();
        ordersSteps.setOrderID();
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

    @TestRailID(id = 2)
    @ParameterizedTest
    @CsvSource({"Order4190168data.json, Order4190168dataUpdate.json"})
    public void orderUpdateAddProduct(String newOrderJson, String updateOrderJson, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int idNew = stageOrderSteps.insertJsonToTableAndContext(newOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
        OrderLineSteps orderLineSteps = new OrderLineSteps();
        new OrdersSteps().setOrderID();
        OrderItem product1 = getLwaTestContext(testInfo).getItem("REVOFIL AQUASHINE BTX");
        errorMessage.append(orderLineSteps.checkOrderLineTableAndSetWarehouseOrderID(product1));

        errorMessage.append(orderLineSteps.checkProductIsAbsent(StringEscapeUtils.unescapeJava("EYLEA\\u00ae 40mg/1ml Non-English")));
        int idUpdate = stageOrderSteps.insertJsonToTableAndContext(updateOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());

        OrderItem product2 = getLwaTestContext(testInfo).getItem(StringEscapeUtils.unescapeJava("EYLEA\\u00ae 40mg/1ml Non-English"));
        new WarehouseOrderSteps().setWarehouseOrders();
        errorMessage.append(orderLineSteps.checkOrderLineTableWithWarehouseOrderID(product2));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        stageOrderSteps.deleteRow(idNew);
        stageOrderSteps.deleteRow(idUpdate);
    }

    @TestRailID(id = 422)
    @ParameterizedTest
    @CsvSource({"Order9990002data.json,  Order9990002dataUpdate.json"})
    public void orderUpdateProductRemoved(String newOrderJson, String updateOrderJson, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int idNew = stageOrderSteps.insertJsonToTableAndContext(newOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());

        new OrdersSteps().setOrderID();
        WarehouseOrderSteps warehouseOrderSteps = new WarehouseOrderSteps();
        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderQuantity(2));
        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderTable());

        int idUpdate = stageOrderSteps.insertJsonToTableAndContext(updateOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());
        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderIsNotActive("Bulgarium"));

        stageOrderSteps.deleteRow(idNew);
        stageOrderSteps.deleteRow(idUpdate);

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 3523)
    @ParameterizedTest
    @CsvSource({"Order1081869.json, Order1081869Cancel.json"})
    public void cancelOrder(String newOrderJson, String updateOrderJson, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int idNew = stageOrderSteps.insertJsonToTableAndContext(newOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());

        OrdersSteps ordersSteps = new OrdersSteps();
        ordersSteps.setOrderID();
        errorMessage.append(ordersSteps.checkOrdersTable());

        WarehouseOrderSteps warehouseOrderSteps = new WarehouseOrderSteps();
        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderStatusesIsActive());

        int idUpdate = stageOrderSteps.insertJsonToTableAndContext(updateOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());

        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderIsNotActive("Nickel-28-Ni"));
        errorMessage.append(ordersSteps.checkOrderIsCancelled());

        stageOrderSteps.deleteRow(idNew);
        stageOrderSteps.deleteRow(idUpdate);

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 3537)
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

    @TestRailID(id = 3930)
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

    @TestRailID(id = 39704)
    @Test
    public void getWarehouseOrdersNoCriteria(TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        StageOrderSteps stageOrderSteps = new StageOrderSteps();

        for (GetWarehouseOrderNoCriteriaEnum json : GetWarehouseOrderNoCriteriaEnum.values()) {
            int idNew = stageOrderSteps.insertJsonToTableAndContext(json.getTitle(), testInfo);
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

    @TestRailID(id = 39705)
    @Test
    public void getWarehouseOrdersAllsysOrderId(TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        StageOrderSteps stageOrderSteps = new StageOrderSteps();

        for (GetWarehouseOrderNoCriteriaEnum json : GetWarehouseOrderNoCriteriaEnum.values()) {
            int idNew = stageOrderSteps.insertJsonToTableAndContext(json.getTitle(), testInfo);
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

    //https://statystech.atlassian.net/browse/LWA-963
//    @TestRailID(id = 39706)
//    @Test
//    public void getWarehouseOrdersAllsysOrderIdIgnoreOtherCriteria(TestInfo testInfo) throws IOException, SQLException {
//        StringBuilder errorMessage = new StringBuilder();
//        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
//        StageOrderSteps stageOrderSteps = new StageOrderSteps();
//
//        int idNew = stageOrderSteps.insertJsonToTableAndContext(GetWarehouseOrderNoCriteriaEnum.order6097147.getTitle(), testInfo);
//        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
//
//        ArrayList<Integer> expectedOrderNumbersList = new ArrayList<>(Collections.singletonList(6097147));
//        OrdersTable ordersTable = new OrdersTable();
//        for (int orderAllSysID : expectedOrderNumbersList) {
//            assertTrue(ordersTable.checkRowWithValueIsPresent("orderAllSysID", String.valueOf(orderAllSysID)));
//        }
//
//        LwaApiSteps lwaApiSteps = new LwaApiSteps();
//
//        errorMessage.append(lwaApiSteps.updateLwaContextWithWarehouseSearchResult(ApiRestUtils.getWarehouseOrders(6097147, "EU", "ROTW"), lwaTestContext));
//        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
//
//        errorMessage.append(lwaApiSteps.checkWarehouseSearchResponse(
//                new ArrayList<>(Collections.singletonList(6097147)), lwaTestContext));
//        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
//    }

    @TestRailID(id = 40873)
    @Test
    public void getWarehouseOrdersAllsysOrderIdOtherCriteria(TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        StageOrderSteps stageOrderSteps = new StageOrderSteps();

        for (GetWarehouseOrderNoCriteriaEnum json : GetWarehouseOrderNoCriteriaEnum.values()) {
            int idNew = stageOrderSteps.insertJsonToTableAndContext(json.getTitle(), testInfo);
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

    @TestRailID(id = 40874)
    @Test
    public void getWarehouseOrdersWrongUserRole(TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        LwaApiSteps lwaApiSteps = new LwaApiSteps();

        errorMessage.append(lwaApiSteps.updateLwaContextWithWarehouseSearchResult(ApiRestUtils.getWarehouseOrders(6097147, "EU", "ROTW"), lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }
}
