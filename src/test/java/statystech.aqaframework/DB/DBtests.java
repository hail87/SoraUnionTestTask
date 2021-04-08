package statystech.aqaframework.DB;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.steps.APIsteps.StageOrderApiSteps;
import statystech.aqaframework.steps.DBsteps.*;
import statystech.aqaframework.utils.DBUtils;

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
//        errorMessage.append(new UserTableSteps().checkAllSysUserIDColumn());
        errorMessage.append(new ShippingAddressSteps().checkShippingAddress());
//        errorMessage.append();
//        errorMessage.append();
//        errorMessage.append();
//        errorMessage.append();
//        errorMessage.append();
        new DBUtils().closeConnection();
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        //dBsteps.cleanAndFinish();
    }

}
