package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ProductJson.ItemsItem;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;

public class ProductDescriptionSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(ProductDescriptionSteps.class);

    public String checkProductDescription(ItemsItem item) {
        String actual = DBUtils.executeAndReturnString(String.format("select productDescription from productDescription where productID = '%s'", item.getProductIdFromDB()));
        if (actual.isEmpty()) {
            return String.format("\n [checkProductDescription]: There is no product with productID %s found at the productDescription table", item.getProductIdFromDB());
        }
        String expected = DataUtils.convertUnicodeToAscii(item.getProductNameEng());
        return verifyExpectedResults(actual, expected);
    }
}