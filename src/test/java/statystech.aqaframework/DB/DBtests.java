package statystech.aqaframework.DB;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.utils.DataUtils;
import statystech.aqaframework.DataObjects.DBUser;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.steps.ApiSteps;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBtests {


    @ParameterizedTest
    @ValueSource(strings = {"order1000100data.json"})
    public void orderStatusCheck(String jsonFilename) throws IOException, SQLException {
        DBUtils dbUtils = new DBUtils();
        dbUtils.connectDB(new DBUser());
        String jsonContent = new DataUtils().getJsonContent(jsonFilename);
        dbUtils.insertJsonToStageOrder(jsonContent);
        assertTrue(new ApiSteps().triggerOrderProcessingSandBox());
    }

}
