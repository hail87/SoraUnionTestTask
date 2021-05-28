package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.DataObjects.ProductJson.ItemsItem;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;

public class ProductDescriptionSteps extends Steps {

    public String checkProductDescription(ItemsItem item) {
        String actual = null;
        try {
            actual = DBUtils.executeAndReturnString(String.format("select productDescription from productDescription where productID = '%s'", item.getProductIdFromDB()));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return String.format("\n [checkBatchNumber]: There is no product with productID %s found at the productDescription table", item.getProductIdFromDB());
        }
        String expected = DataUtils.convertUnicodeToAscii(item.getProductNameEng());
        return verifyExpectedResults(actual, expected);
    }
}
