package statystech.aqaframework.tests.DB;


import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.DataObjects.Jackson.OrderItem;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.TestRail.TestRailSteps;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.DBsteps.*;
import statystech.aqaframework.tests.TestClass;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestRailReportExtension.class)
public class LwaTestSuite extends TestClass {

    @TestRailID(id=1)
    @ParameterizedTest
    @ValueSource(strings = {"order1000100data.json"})
    public void newOrderProcessing(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
        errorMessage.append(new OrdersSteps().checkOrdersTable());
        errorMessage.append(new UserTableSteps().checkAllSysUserIDColumn());
        errorMessage.append(new ShippingAddressSteps().checkShippingAddressTable());
        errorMessage.append(new BuyerSteps().checkBuyerBillingInformation());
        errorMessage.append(new ShopperGroupSteps().checkShopperGroupTable());
        for (OrderItem item : getTestContext(testInfo).getOrder().getOrderItems()) {
            errorMessage.append(new ProductSteps().checkProduct(item));
            errorMessage.append(new ProductBatchSteps().checkBatchNumber(item));
            errorMessage.append(new OrderLineSteps().checkOrderLineTableAndSetWarehouseOrderID(item));
        }
        errorMessage.append(new WarehouseOrderSteps().checkWarehouseOrderTable());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id=2)
    @ParameterizedTest
    @CsvSource({"Order4190168data.json, Order4190168dataUpdate.json"})
    public void orderUpdateAddProduct(String newOrderJson, String updateOrderJson, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int idNew = stageOrderSteps.insertJsonToTableAndContext(newOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
        OrderLineSteps orderLineSteps = new OrderLineSteps();
        new OrdersSteps();
        OrderItem product1 = Context.getTestContext(testInfo).getItem("REVOFIL AQUASHINE BTX");
        errorMessage.append(orderLineSteps.checkOrderLineTableAndSetWarehouseOrderID(product1));

        errorMessage.append(orderLineSteps.checkProductIsAbsent(StringEscapeUtils.unescapeJava("EYLEA\\u00ae 40mg/1ml Non-English")));
        int idUpdate = stageOrderSteps.insertJsonToTableAndContext(updateOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());

        OrderItem product2 = Context.getTestContext(testInfo).getItem(StringEscapeUtils.unescapeJava("EYLEA\\u00ae 40mg/1ml Non-English"));
        new WarehouseOrderSteps().setWarehouseOrders();
        errorMessage.append(orderLineSteps.checkOrderLineTableWithWarehouseOrderID(product2));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        stageOrderSteps.deleteRow(idNew);
        stageOrderSteps.deleteRow(idUpdate);
    }

    @TestRailID(id=422)
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
    }

    @TestRailID(id=3523)
    @ParameterizedTest
    @CsvSource({"Order1081869.json, Order1081869Cancel.json"})
    public void cancelOrder(String newOrderJson, String updateOrderJson, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int idNew = stageOrderSteps.insertJsonToTableAndContext(newOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());

        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.checkOrdersTable());

        WarehouseOrderSteps warehouseOrderSteps = new WarehouseOrderSteps();
        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderStatusesIsActive());

        int idUpdate = stageOrderSteps.insertJsonToTableAndContext(updateOrderJson, testInfo);
        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());

        errorMessage.append(warehouseOrderSteps.checkWarehouseOrderIsNotActive("Nickel-28-Ni"));
        errorMessage.append(ordersSteps.checkOrderIsCancelled());

        stageOrderSteps.deleteRow(idNew);
        stageOrderSteps.deleteRow(idUpdate);
    }
//
//    @Disabled("Disabled until done")
//    //@TestRailID(id=3537)
//    @ParameterizedTest
//    @ValueSource(strings = {"ProductsSmall.json"})
//    public void addProductsTest(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
//        StringBuilder errorMessage = new StringBuilder();
//        CommonDbSteps dBsteps = new CommonDbSteps();
//        dBsteps.connectDB();
//        StageProductSteps stageProductSteps = new StageProductSteps();
//        int id = stageProductSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
//        assertTrue(stageProductSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());
//
////        errorMessage.append(new OrdersSteps().checkOrdersTable());
////        errorMessage.append(new UserTableSteps().checkAllSysUserIDColumn());
////        errorMessage.append(new ShippingAddressSteps().checkShippingAddressTable());
////        errorMessage.append(new BuyerSteps().checkBuyerBillingInformation());
////        errorMessage.append(new ShopperGroupSteps().checkShopperGroupTable());
////        for (Product product : TestContext.products) {
////            errorMessage.append(new ProductSteps().checkProduct(product));
////            errorMessage.append(new ProductBatchSteps().checkBatchNumber(product));
////            errorMessage.append(new OrderLineSteps().checkOrderLineTableAndSetWarehouseOrderID(product));
////        }
////        errorMessage.append(new WarehouseOrderSteps().checkWarehouseOrderTable());
//
//        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
//        stageProductSteps.deleteRow(id);
//        //TODO: delete all new rows
//        dBsteps.closeConnection();
//    }

//    @Disabled("Disabled until done")
//    @TestRailID(id=3930)
//    @ParameterizedTest
//    @CsvSource({"Products.json, ProductsUpdate.json"})
//    public void updateProducts(String newOrderJson, String updateOrderJson) throws IOException, SQLException {
//        StringBuilder errorMessage = new StringBuilder();
//        CommonDbSteps dBsteps = new CommonDbSteps();
//        dBsteps.connectDB();
//        StageOrderSteps stageOrderSteps = new StageOrderSteps();
//        int idNew = stageOrderSteps.insertJsonToTableAndContext(newOrderJson);
//        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
//        OrderLineSteps orderLineSteps = new OrderLineSteps();
//
//        Product product1 = JsonUtils.getJsonProductWithName("REVOFIL AQUASHINE BTX");
//        errorMessage.append(orderLineSteps.checkOrderLineTableAndSetWarehouseOrderID(product1));
//
//        errorMessage.append(orderLineSteps.checkProductIsAbsent("EYLEA\\u00ae 40mg/1ml Non-English"));
//
//        int idUpdate = stageOrderSteps.insertJsonToTableAndContext(updateOrderJson);
//        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());
//
//        Product product2 = JsonUtils.getJsonProductWithName(StringEscapeUtils.unescapeJava("EYLEA\\u00ae 40mg/1ml Non-English"));
//        new WarehouseOrderSteps().setWarehouseOrders();
//        errorMessage.append(orderLineSteps.checkOrderLineTableWithWarehouseOrderID(product2));
//
//        stageOrderSteps.deleteRow(idNew);
//        stageOrderSteps.deleteRow(idUpdate);
//        dBsteps.closeConnection();
//        TestContext.cleanContext();
//    }
//
//    @Test
//    public void closeTestRailOpenRuns(){
//        new TestRailSteps().closeAllOpenTestRuns();
//    }

}
