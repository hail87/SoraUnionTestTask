package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.DBUser;
import statystech.aqaframework.common.ConnectionDB;
import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;

public class CommonDbSteps {

    private static final Logger logger = LoggerFactory.getLogger(StageOrderSteps.class);

    public void connectDB() throws IOException {
        new ConnectionDB().connectDB(new DBUser());
        logger.info("Data Base connected!");
    }

    public void cleanAndFinish(int id) {
        DBUtils dbUtils = new DBUtils();
        dbUtils.deleteRow(id);
        dbUtils.closeConnection();
    }
}
