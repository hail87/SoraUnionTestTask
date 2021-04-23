package statystech.aqaframework.steps.DBsteps;


import org.apache.commons.lang3.StringEscapeUtils;
import statystech.aqaframework.TableObjects.WarehouseOrderTable;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.JsonUtils;

import java.sql.SQLException;

public class WarehouseOrderSteps extends Steps {

    public String checkWarehouseOrderTable() throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkComments());
        return errorMessage.toString();
    }

    private String checkComments() {
        String actualComments = "";
        try {
            actualComments = new WarehouseOrderTable().getColumnValue(TestContext.warehouseOrderID, "comments");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String expectedComments = StringEscapeUtils.unescapeJava(JsonUtils.getValueFromJSON("shipping_notes"));
        return verifyExpectedResults(actualComments, expectedComments);
    }
}
