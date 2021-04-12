package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.BuyerTable;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.TableObjects.ShippingAddressTable;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class BuyerSteps extends Steps {

    public String checkBuyerBillingInformation() throws SQLException {
        BuyerTable buyerTable = new BuyerTable();
        StringBuilder errorMessage = new StringBuilder();
        int buyerID = buyerTable.getPrimaryID();
        errorMessage.append(verifyExpectedResults(
                buyerTable.getJsonAndTableValue(buyerID, "billing_address", "address_line_1")));
        errorMessage.append(verifyExpectedResults(
                buyerTable.getJsonAndTableValue(buyerID, "billing_address", "address_line_2")));
        errorMessage.append(verifyExpectedResults(
                buyerTable.getJsonAndTableValue(buyerID, "billing_address", "city")));
        errorMessage.append(verifyExpectedResults(
                buyerTable.getJsonAndTableValue(buyerID, "billing_address", "country")));
        errorMessage.append(verifyExpectedResults(
                buyerTable.getJsonAndTableValue(buyerID, "billing_address", "address_type")));
        errorMessage.append(verifyExpectedResults(
                buyerTable.getJsonAndTableValue(buyerID, "billing_address", "first_name")));
        errorMessage.append(verifyExpectedResults(
                buyerTable.getJsonAndTableValue(buyerID, "billing_address", "last_name")));
        errorMessage.append(checkAllSysBuyerID());
        errorMessage.append(checkRegion());
        return errorMessage.toString();
    }

    private String checkAllSysBuyerID() throws SQLException {
        String actualPhoneNumber = new BuyerTable().getColumnValue("allSysBuyerID");
        String expectedPhoneNumber = TestContext.JSON_OBJECT.getAsJsonObject("billing_address").get("user_info_id").toString().replace("\"", "");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }

    private String checkRegion() throws SQLException {
        String actualPhoneNumber = new BuyerTable().getColumnValue("region");
        String expectedPhoneNumber = TestContext.JSON_OBJECT.getAsJsonObject("billing_address").get("state").toString().replace("\"", "");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }

}
