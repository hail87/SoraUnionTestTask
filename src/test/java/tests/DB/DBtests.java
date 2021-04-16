package tests.DB;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.APIsteps.StageOrderApiSteps;
import statystech.aqaframework.steps.DBsteps.BuyerSteps;
import statystech.aqaframework.steps.DBsteps.CommonDbSteps;
import statystech.aqaframework.steps.DBsteps.OrderLineSteps;
import statystech.aqaframework.steps.DBsteps.OrdersSteps;
import statystech.aqaframework.steps.DBsteps.ProductBatchSteps;
import statystech.aqaframework.steps.DBsteps.ProductSteps;
import statystech.aqaframework.steps.DBsteps.ShippingAddressSteps;
import statystech.aqaframework.steps.DBsteps.ShopperGroupSteps;
import statystech.aqaframework.steps.DBsteps.StageOrderSteps;
import statystech.aqaframework.steps.DBsteps.UserTableSteps;
import statystech.aqaframework.steps.DBsteps.WarehouseOrderSteps;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBtests {


    @ParameterizedTest
    @ValueSource(strings = {"order1095793dataQuattro.json"})
    public void orderStatusCheck(String jsonFilename) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        CommonDbSteps dBsteps = new CommonDbSteps();
        dBsteps.connectDB();
        int id = stageOrderSteps.insertJsonToStageOrderTable(jsonFilename);
        errorMessage.append(new StageOrderApiSteps().triggerOrderProcessingSandBox());
        errorMessage.append(new StageOrderSteps().checkStatusColumn(id));
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

}
