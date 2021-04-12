package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.BuyerTable;
import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.TableObjects.ShippingAddressTable;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class ShippingAddressSteps extends Steps {

    public String checkShippingAddressTable() throws SQLException {
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
        errorMessage.append(checkPhoneNumber());
        errorMessage.append(checkPostalCode());
        errorMessage.append(checkRegion());

        return errorMessage.toString();
    }

    private String checkPhoneNumber() throws SQLException {
        String actualPhoneNumber = new ShippingAddressTable().getColumnValue("phoneNumber1");
        String expectedPhoneNumber = TestContext.JSON_OBJECT.getAsJsonObject("shipping_address").get("phone_1").toString().replace("\"", "");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }

    private String checkPostalCode() throws SQLException {
        String actualPhoneNumber = new ShippingAddressTable().getColumnValue("postalCode");
        String expectedPhoneNumber = TestContext.JSON_OBJECT.getAsJsonObject("shipping_address").get("zip").toString().replace("\"", "");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }

    private String checkRegion() throws SQLException {
        String actualPhoneNumber = new BuyerTable().getColumnValue("region");
        String expectedPhoneNumber = TestContext.JSON_OBJECT.getAsJsonObject("shipping_address").get("state").toString().replace("\"", "");
        return verifyExpectedResults(actualPhoneNumber, expectedPhoneNumber);
    }
}
