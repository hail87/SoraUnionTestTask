package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.StageOrderTable;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
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

    public int insertJsonToStageOrderTableAndContext(String jsonFilename) throws IOException, SQLException {
        DBUtils dbUtils = new DBUtils();
        String jsonContent = new JsonUtils().getJsonContentAndLoadToContext(jsonFilename);
        logger.info("Inserting json to the stageOrder table");
        return dbUtils.insertJsonToStageOrder(jsonContent);
    }

    public boolean deleteRow(int stageOrderId) {
        return new StageOrderTable().deleteRow(stageOrderId);
    }
}
