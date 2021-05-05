package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.StageOrderTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;

public class StageProductSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(StageProductSteps.class);

    public void triggerProcessingSandBox() {
        logger.info("Triggering stageProduct processing at the SandBox");
        if (!new ApiRestUtils().sendGetRequest(
                "http://8a57e667-lwasandbox-ingres-3266-559840864.us-east-1.elb.amazonaws.com/start"))
            logger.error("Response code != 200");
    }

    public String checkStatusColumn(int rowID) throws SQLException {
        if (new DBUtils().select("stageProduct", rowID, "status").equalsIgnoreCase("C")) {
            return "";
        } else {
            return "Status at the Status column isn't equal to 'C'";
        }
    }

    public int insertJsonToTableAndContext(String jsonFilename, int testRailID) throws IOException, SQLException {
        String jsonContent = new JsonUtils().getProductJsonObjectsAndLoadToContext(jsonFilename, testRailID);
        logger.info("Inserting json to the stageProduct table");
        int id = new DBUtils().insertJsonToStageProduct(jsonContent);
        triggerProcessingSandBox();
        return id;
    }

    public boolean deleteRow(int stageOrderId) {
        return new StageOrderTable().deleteRow(stageOrderId);
    }
}
