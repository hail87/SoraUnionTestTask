package statystech.aqaframework.steps.DBsteps;

import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.StageOrderTable;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.JsonUtils;
import statystech.aqaframework.steps.Steps;

import java.io.IOException;
import java.sql.SQLException;

public class StageOrderSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(StageOrderSteps.class);

    public void triggerProcessingSandBox() {
        logger.info("Triggering order processing at the SandBox");
        if (!new ApiRestUtils().sendGetRequest(
                "http://8a57e667-lwasandbox-ingres-db29-1956677137.us-east-1.elb.amazonaws.com/start"))
            logger.error("Response code != 200");
    }

    public String checkStatusColumn(int rowID) throws SQLException {
        if (new DBUtils().select("stageOrder", rowID, "status").equalsIgnoreCase("C")) {
            return "";
        } else {
            return "Status at the Status column isn't equal to 'C'";
        }
    }

    public int insertJsonToTableAndContext(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        DBUtils dbUtils = new DBUtils();
        String jsonContent = new JsonUtils().loadObjectToContextAndGetString(jsonFilename, testInfo.getTestMethod().get().getName());
        logger.info("Inserting json to the stageOrder table");
        int id = dbUtils.insertJsonToStageOrder(jsonContent);
        new StageOrderSteps().triggerProcessingSandBox();
        return id;
    }

    public boolean deleteRow(int stageOrderId) {
        return new StageOrderTable().deleteRow(stageOrderId);
    }
}
