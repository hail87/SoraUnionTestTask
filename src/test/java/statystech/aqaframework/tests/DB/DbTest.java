package statystech.aqaframework.tests.DB;


import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
    public void orderStatusCheck(String jsonFilename) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        CommonDbSteps dBsteps = new CommonDbSteps();
        dBsteps.connectDB();
        int id = stageOrderSteps.insertJsonToStageOrderTable(jsonFilename);
        errorMessage.append(new StageOrderApiSteps().triggerOrderProcessingSandBox());
        assertTrue(new StageOrderSteps().checkStatusColumn(id).isEmpty(), errorMessage.toString());
        errorMessage.append(new OrdersSteps().checkOrdersTable());
        errorMessage.append(new UserTableSteps().checkAllSysUserIDColumn());
        errorMessage.append(new ShippingAddressSteps().checkShippingAddressTable());
        errorMessage.append(new BuyerSteps().checkBuyerBillingInformation());
        errorMessage.append(new WarehouseOrderSteps().checkWarehouseOrderTable());
        errorMessage.append(new ShopperGroupSteps().checkShopperGroupTable());
        JsonUtils.makeProductObjectsFromJson();
        for (Product product : TestContext.products) {
            errorMessage.append(new ProductSteps().checkProduct(product));
            errorMessage.append(new ProductBatchSteps().checkBatchNumber(product));
            errorMessage.append(new OrderLineSteps().checkOrderLineTable(product));
        }
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        dBsteps.cleanAndFinish(id);
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
