package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.utils.DBUtils;

import java.sql.SQLException;

public class StageOrderSteps extends DBUtils {

    private static final Logger logger = LoggerFactory.getLogger(StageOrderSteps.class);

    public String checkStatusColumn(int rowID) throws SQLException {
        if (select("stageOrder", rowID, "status").equalsIgnoreCase("C")) {
            return "";
        } else {
            return "Status at the Status column isn't equal to 'C'";
        }
    }
}
