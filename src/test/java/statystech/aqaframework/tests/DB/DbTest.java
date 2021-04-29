package statystech.aqaframework.tests.DB;


import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.APIsteps.StageOrderApiSteps;
import statystech.aqaframework.steps.DBsteps.*;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestRailReportExtension.class)
public class DbTest {

    @TestRailID(id="1")
    @ParameterizedTest
    @ValueSource(strings = {"order1000100data.json"})
    public void newOrderProcessing(String jsonFilename) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        CommonDbSteps dBsteps = new CommonDbSteps();
        dBsteps.connectDB();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToStageOrderTableAndContext(jsonFilename);
        errorMessage.append(new StageOrderApiSteps().triggerOrderProcessingSandBox());
        assertTrue(new StageOrderSteps().checkStatusColumn(id).isEmpty(), errorMessage.toString());
        errorMessage.append(new OrdersSteps().checkOrdersTable());
        errorMessage.append(new UserTableSteps().checkAllSysUserIDColumn());
        errorMessage.append(new ShippingAddressSteps().checkShippingAddressTable());
        errorMessage.append(new BuyerSteps().checkBuyerBillingInformation());
        errorMessage.append(new ShopperGroupSteps().checkShopperGroupTable());
        for (Product product : TestContext.products) {
            errorMessage.append(new ProductSteps().checkProduct(product));
            errorMessage.append(new ProductBatchSteps().checkBatchNumber(product));
            errorMessage.append(new OrderLineSteps().checkOrderLineTableAndSetWarehouseOrderID(product));
        }
        errorMessage.append(new WarehouseOrderSteps().checkWarehouseOrderTable());

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        stageOrderSteps.deleteRow(id);
        //TODO: delete all new rows
        dBsteps.closeConnection();
    }

    @TestRailID(id="2")
    @ParameterizedTest
    @CsvSource({"Order4190168data.json, Order4190168dataUpdate.json"})
    public void orderUpdate(String newOrderJson, String updateOrderJson) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        CommonDbSteps dBsteps = new CommonDbSteps();
        dBsteps.connectDB();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int idNew = stageOrderSteps.insertJsonToStageOrderTableAndContext(newOrderJson);
        errorMessage.append(new StageOrderApiSteps().triggerOrderProcessingSandBox());
        assertTrue(new StageOrderSteps().checkStatusColumn(idNew).isEmpty(), errorMessage.toString());
        OrderLineSteps orderLineSteps = new OrderLineSteps();

        Product product1 = JsonUtils.getJsonProductWithName("REVOFIL AQUASHINE BTX");
        errorMessage.append(orderLineSteps.checkOrderLineTableAndSetWarehouseOrderID(product1));

        int idUpdate = stageOrderSteps.insertJsonToStageOrderTableAndContext(updateOrderJson);
        Product product2 = JsonUtils.getJsonProductWithName(StringEscapeUtils.unescapeJava("EYLEA\\u00ae 40mg/1ml Non-English"));
        errorMessage.append(orderLineSteps.checkProductIsAbsent(product2));
        errorMessage.append(new StageOrderApiSteps().triggerOrderProcessingSandBox());
        assertTrue(new StageOrderSteps().checkStatusColumn(idUpdate).isEmpty(), errorMessage.toString());

        errorMessage.append(orderLineSteps.checkOrderLineTableWithWarehouseOrderID(product2));

        stageOrderSteps.deleteRow(idNew);
        stageOrderSteps.deleteRow(idUpdate);
        dBsteps.closeConnection();
    }

//    @Test
//    @TestRailID(id="1")
//    public void testCanary(){
//        assertTrue(true);
//    }
//
//    @Test
//    @TestRailID(id="2")
//    public void test2(){
//        assertTrue(true);
//    }

}
