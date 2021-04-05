package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.ConnectionDB;
import statystech.aqaframework.utils.DBUtils;

import java.sql.SQLException;

public class StageOrderDBSteps extends DBUtils {

    private static final Logger logger = LoggerFactory.getLogger(StageOrderDBSteps.class);

    public boolean checkStatusColumn(int rowID) throws SQLException {
        return select("stageOrder", rowID, "status").equalsIgnoreCase("C");
    }
}
