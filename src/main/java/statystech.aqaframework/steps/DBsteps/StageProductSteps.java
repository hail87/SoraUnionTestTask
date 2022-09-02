package statystech.aqaframework.steps.DBsteps;

import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.StageOrderTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;

public class StageProductSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(StageProductSteps.class);

    public void triggerProcessingSandBox() {
        logger.info("Triggering stageProduct processing at the SandBox");
        int responseCode = new ApiRestUtils().sendGetRequest(
                DataUtils.getPropertyValue("url.properties", "stageProductProcessingTrigger"));
        if (responseCode != 200)
            logger.error(String.format("\nResponse code != 200, actual response code : %d", responseCode));
    }

    public String checkStatusColumn(int rowID) throws SQLException {
        if (new DBUtils().select("stageProduct", rowID, "status").equalsIgnoreCase("C")) {
            return "";
        } else {
            return "Status at the Status column isn't equal to 'C'";
        }
    }

    public int insertJsonToTableAndContext(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        String jsonContent = new JsonUtils().getProductJsonObjectAndLoadToContext(jsonFilename, testInfo.getTestMethod().get().getName());
        String encryptedJsonContent = DataUtils.encryptForSandbox(jsonContent);
        logger.info("Inserting json to the stageProduct table");
        int id = new DBUtils().insertJsonToStageProduct(encryptedJsonContent);
        triggerProcessingSandBox();
        return id;
    }

    public int insertJsonToTable(String jsonFilename) throws IOException, SQLException {
        String jsonContent = JsonUtils.getStringFromJson(jsonFilename);
        String encryptedJsonContent = DataUtils.encryptForSandbox(jsonContent);
        logger.info("Inserting json to the stageProduct table");
        int id = new DBUtils().insertJsonToStageProduct(encryptedJsonContent);
        triggerProcessingSandBox();
        return id;
    }

    public boolean deleteRow(int stageOrderId) {
        return new StageOrderTable().deleteRow(stageOrderId);
    }
}
