package statystech.aqaframework.steps.DBsteps;


import statystech.aqaframework.TableObjects.BuyerTable;
import statystech.aqaframework.TableObjects.ShippingAddressTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class BuyerSteps extends Steps {

    BuyerTable buyerTable = new BuyerTable();

    public String checkBuyerBillingInformation() throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        int buyerID = buyerTable.getBuyerID();
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
        errorMessage.append(checkPhoneNumber(buyerID));
        errorMessage.append(checkPostalCode(buyerID));
        errorMessage.append(checkRegion(buyerID));
        return errorMessage.toString();
    }

    private String checkRegion(int buyerID) throws SQLException {
        String actual = buyerTable.getColumnValueByPrimaryID(buyerID,"region");
        String expected = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject("billing_address").get("state").toString().replace("\"", "");
        return verifyExpectedResults(actual, expected);
    }

    private String checkPhoneNumber(int buyerID) throws SQLException {
        String actual = buyerTable.getColumnValueByPrimaryID(buyerID,"phoneNumber1");
        String expected = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject("shipping_address").get("phone_1").toString().replace("\"", "");
        return verifyExpectedResults(actual, expected);
    }

    private String checkPostalCode(int buyerID) throws SQLException {
        String actual = buyerTable.getColumnValueByPrimaryID(buyerID,"postalCode");
        String expected = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject("billing_address").get("zip").toString().replace("\"", "");
        return verifyExpectedResults(actual, expected);
    }

}
