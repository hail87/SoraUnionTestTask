package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.DataObjects.DBUser;
import statystech.aqaframework.common.ConnectionDB;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;

public class CommonDbSteps {

    public int insertJsonToStageOrderTable(String jsonFilename) throws IOException, SQLException {
        DBUtils dbUtils = new DBUtils();
        String jsonContent = new DataUtils().getJsonContent(jsonFilename);
        new ConnectionDB().connectDB(new DBUser());
        return dbUtils.insertJsonToStageOrder(jsonContent);
    }

    public void cleanAndFinish(int id) {
        DBUtils dbUtils = new DBUtils();
        dbUtils.deleteRow(id);
        dbUtils.closeConnection();
    }
}
