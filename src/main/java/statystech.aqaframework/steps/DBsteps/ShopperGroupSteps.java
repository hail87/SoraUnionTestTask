package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.TableObjects.ShopperGroupTable;
import statystech.aqaframework.steps.Steps;

import java.io.IOException;
import java.sql.SQLException;

public class ShopperGroupSteps extends Steps {

    public String checkShopperGroupTable() throws SQLException {
        ShopperGroupTable shopperGroupTable = new ShopperGroupTable();
        StringBuilder errorMessage = new StringBuilder();
        int orderLineID = 0;
        try {
            orderLineID = new OrdersTable().getShopperGroupIDValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        errorMessage.append(verifyExpectedResults(
                shopperGroupTable.getJsonAndTableValue(orderLineID, "shopper_group_name")));
        return errorMessage.toString();
    }
}
