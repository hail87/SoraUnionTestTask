package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.TableObjects.WarehouseOrderTable;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.JsonUtils;

import java.sql.SQLException;

public class WarehouseOrderSteps extends Steps {

    public String checkWarehouseOrderTable() throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(checkComments());
        return errorMessage.toString();
    }

    private String checkComments() throws SQLException {
        String actualComments = new WarehouseOrderTable().getColumnValue("comments");
        String expectedComments = JsonUtils.getValueFromJSON("shipping_notes");
        return verifyExpectedResults(actualComments, expectedComments);
    }
}
