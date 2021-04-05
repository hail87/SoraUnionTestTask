package statystech.aqaframework.DB;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.steps.DBsteps.DBsteps;
import statystech.aqaframework.steps.APIsteps.StageOrderApiSteps;
import statystech.aqaframework.steps.DBsteps.StageOrderDBSteps;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBtests {


    @ParameterizedTest
    @ValueSource(strings = {"order1000100data.json"})
    public void orderStatusCheck(String jsonFilename) throws IOException, SQLException {
        DBsteps dBsteps = new DBsteps();
        int id = dBsteps.insertJsonToStageOrderTable(jsonFilename);
        assertTrue(new StageOrderApiSteps().triggerOrderProcessingSandBox());
        assertTrue(new StageOrderDBSteps().checkStatusColumn(id));
        dBsteps.cleanAndFinish(id);
    }

}
