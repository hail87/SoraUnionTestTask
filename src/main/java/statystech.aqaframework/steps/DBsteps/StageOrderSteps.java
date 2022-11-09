package statystech.aqaframework.steps.DBsteps;

import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.StageOrderTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;
import statystech.aqaframework.utils.JsonUtils;
import statystech.aqaframework.steps.Steps;

import java.io.IOException;
import java.sql.SQLException;

public class StageOrderSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(StageOrderSteps.class);

    public void triggerProcessingSandBox() {
        logger.info("Triggering order processing at the SandBox");
        int responseCode = 0;
        int i = 0;
        while (responseCode == 0 & i < 3) {
            delay(3000);
            try {
                responseCode = new ApiRestUtils().sendGetRequest(
                        DataUtils.getPropertyValue("url.properties", "stageOrderProcessingTrigger"));
            } catch (Exception e) {
                logger.warn(e.getLocalizedMessage());
            }
            i++;
        }

        if (responseCode != 200)
            logger.error(String.format("\nResponse code != 200, actual response code : %d", responseCode));
    }

    public void triggerProcessingQA() {
        logger.info("Triggering order processing at the SandBox");
        int responseCode = 0;
        int i = 0;
        while (responseCode == 0 & i < 3) {
            delay(3000);
            try {
                responseCode = new ApiRestUtils().sendGetRequest(
                        DataUtils.getPropertyValue("url.properties", "stageOrderProcessingTriggerQA"));
            } catch (Exception e) {
                logger.warn(e.getLocalizedMessage());
            }
            i++;
        }

        if (responseCode != 200)
            logger.error(String.format("\nResponse code != 200, actual response code : %d", responseCode));
    }

    public String checkStatusColumn(int rowID) {
        return switch (new DBUtils().select("stageOrder", rowID, "status")) {
            case "C" -> "";
            case "E" -> "\nOrder fails into exception\n";
            case "N" -> "\nOrder is still in process\n";
            default -> "Status at the Status column isn't equal to 'C'";
        };
    }

    public int insertJsonToTableAndLwaContext(String jsonFilename, TestInfo testInfo) {
        String jsonContent = null;
        try {
            jsonContent = JsonUtils.loadObjectToContextAndGetString(jsonFilename, testInfo.getTestMethod().get().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String encryptedJsonContent = DataUtils.encryptForSandbox(jsonContent);
        logger.info("Inserting json to the stageOrder table");
        int id = 0;
        try {
            id = new DBUtils().insertJsonToStageOrder(encryptedJsonContent);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        new StageOrderSteps().triggerProcessingSandBox();
        return id;
    }

    public int insertJsonToQATableAndLwaContext(String jsonFilename, TestInfo testInfo) {
        String jsonContent = null;
        try {
            jsonContent = JsonUtils.loadObjectToContextAndGetString(jsonFilename, testInfo.getTestMethod().get().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String encryptedJsonContent = DataUtils.encryptForTest(jsonContent);
        logger.info("Inserting json to the stageOrder table");
        int id = 0;
        try {
            id = new DBUtils().insertJsonToStageOrder(encryptedJsonContent, Context.getTestContext(LwaTestContext.class).getConnectionQA());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        new StageOrderSteps().triggerProcessingQA();
        return id;
    }

    public boolean deleteRow(int stageOrderId) {
        return new StageOrderTable().deleteRow(stageOrderId);
    }
}
