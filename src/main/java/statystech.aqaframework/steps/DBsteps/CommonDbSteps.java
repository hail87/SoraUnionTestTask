package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.DBUser;
import statystech.aqaframework.common.ConnectionDB;
import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;

public class CommonDbSteps {

    private static final Logger logger = LoggerFactory.getLogger(StageOrderSteps.class);

    public void connectDB() throws IOException, SQLException {
        new ConnectionDB().connectDB(new DBUser());
        logger.info("Data Base connected!");
    }

    public void closeConnection() {
        new DBUtils().closeConnection();
    }
}
