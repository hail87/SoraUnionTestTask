package statystech.aqaframework.DB;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.steps.DBsteps.*;

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
        //errorMessage.append(new StageOrderApiSteps().triggerOrderProcessingSandBox());
//        errorMessage.append(new StageOrderSteps().checkStatusColumn(id));
//        errorMessage.append(new OrdersTableSteps().checkOrderID());
      //  errorMessage.append(new UserTableSteps().checkAllSysUserIDColumn());
//        errorMessage.append(new ShippingAddressSteps().checkShippingAddressTable());
//        errorMessage.append(new ProductSteps().checkProduct());
//        errorMessage.append(new BuyerSteps().checkBuyerBillingInformation());
        errorMessage.append(new OrderLineSteps().checkOrderLineTable());
//        errorMessage.append();
//        errorMessage.append();

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        dBsteps.cleanAndFinish(id);
        //        new DBUtils().closeConnection();
    }

}
