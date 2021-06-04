package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.BuyerTable;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.TableObjects.ShippingAddressTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

import java.io.IOException;
import java.sql.SQLException;

public class ShippingAddressSteps extends Steps {

    public String checkShippingAddressTable() throws SQLException, IOException {
        ShippingAddressTable shippingAddressTable = new ShippingAddressTable();
        StringBuilder errorMessage = new StringBuilder();
        int shippingAddressID = new OrdersTable().getShippingAddressIDValue();
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableValue(shippingAddressID, "shipping_address", "address_line_1")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableValue(shippingAddressID, "shipping_address", "address_line_2")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableValue(shippingAddressID, "shipping_address", "city")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableValue(shippingAddressID, "shipping_address", "country")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableValue(shippingAddressID, "shipping_address", "address_type")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableValue(shippingAddressID, "shipping_address", "first_name")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableValue(shippingAddressID, "shipping_address", "last_name")));
        errorMessage.append(checkPhoneNumber(shippingAddressID));
        errorMessage.append(checkPostalCode(shippingAddressID));
        errorMessage.append(checkRegion(shippingAddressID));
        return errorMessage.toString();
    }

    private String checkPhoneNumber(int shippingAddressID) throws SQLException {
        String actualPhoneNumber = new ShippingAddressTable().getColumnValueByPrimaryID(shippingAddressID,"phoneNumber1");
        String expectedPhoneNumber = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject("shipping_address").get("phone_1").toString().replace("\"", "");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }

    private String checkPostalCode(int shippingAddressID) throws SQLException {
        String actualPhoneNumber = new ShippingAddressTable().getColumnValueByPrimaryID(shippingAddressID,"postalCode");
        String expectedPhoneNumber = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject("shipping_address").get("zip").toString().replace("\"", "");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }

    private String checkRegion(int shippingAddressID) throws SQLException {
        String actualPhoneNumber = new BuyerTable().getColumnValueByPrimaryID(shippingAddressID,"region");
        String expectedPhoneNumber = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject("shipping_address").get("state").toString().replace("\"", "");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }
}
