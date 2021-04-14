package tests.DB;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.APIsteps.StageOrderApiSteps;
import statystech.aqaframework.steps.DBsteps.CommonDbSteps;
import statystech.aqaframework.steps.DBsteps.ProductBatchSteps;
import statystech.aqaframework.steps.DBsteps.ProductSteps;
import statystech.aqaframework.steps.DBsteps.StageOrderSteps;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBtests {


    @ParameterizedTest
    @ValueSource(strings = {"order1000100data.json"})
    public void orderStatusCheck(String jsonFilename) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        CommonDbSteps dBsteps = new CommonDbSteps();
        dBsteps.connectDB();
        int id = stageOrderSteps.insertJsonToStageOrderTable(jsonFilename);
        errorMessage.append(new StageOrderApiSteps().triggerOrderProcessingSandBox());
        errorMessage.append(new StageOrderSteps().checkStatusColumn(id));
//        errorMessage.append(new OrdersSteps().checkOrdersTable());
//        errorMessage.append(new UserTableSteps().checkAllSysUserIDColumn());
//        errorMessage.append(new ShippingAddressSteps().checkShippingAddressTable());

//        errorMessage.append(new BuyerSteps().checkBuyerBillingInformation());
//        errorMessage.append(new OrderLineSteps().checkOrderLineTable());
//        errorMessage.append(new WarehouseOrderSteps().checkWarehouseOrderTable()); //bug with encription found, the value at the DB is without unicode
//        errorMessage.append(new ShopperGroupSteps().checkShopperGroupTable());
        JsonUtils.makeProductObjectsFromJson();
        for (Product product : TestContext.products) {
            errorMessage.append(new ProductSteps().checkProduct(product));
            errorMessage.append(new ProductBatchSteps().checkBatchNumber(product));
        }

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        dBsteps.cleanAndFinish(id);
        //        new DBUtils().closeConnection();
    }

}
