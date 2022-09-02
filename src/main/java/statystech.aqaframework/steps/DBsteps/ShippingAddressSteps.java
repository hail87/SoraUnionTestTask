package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.OrdersTable;
import statystech.aqaframework.TableObjects.ShippingAddressTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DataUtils;

import java.sql.SQLException;

public class ShippingAddressSteps extends Steps {

    ShippingAddressTable shippingAddressTable = new ShippingAddressTable();

    public String checkShippingAddressTable() throws SQLException {
        ShippingAddressTable shippingAddressTable = new ShippingAddressTable();
        StringBuilder errorMessage = new StringBuilder();
        int shippingAddressID = new OrdersTable().getShippingAddressIDValue();
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableDecryptValue(shippingAddressID, "shipping_address", "address_line_1")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableDecryptValue(shippingAddressID, "shipping_address", "address_line_2")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableDecryptValue(shippingAddressID, "shipping_address", "city")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableValue(shippingAddressID, "shipping_address", "country")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableValue(shippingAddressID, "shipping_address", "address_type")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableDecryptValue(shippingAddressID, "shipping_address", "first_name")));
        errorMessage.append(verifyExpectedResults(
                shippingAddressTable.getJsonAndTableDecryptValue(shippingAddressID, "shipping_address", "last_name")));
        errorMessage.append(checkPhoneNumber(shippingAddressID));
        errorMessage.append(checkPostalCode(shippingAddressID));
        errorMessage.append(checkRegion(shippingAddressID));
        return errorMessage.toString();
    }

    private String checkPhoneNumber(int shippingAddressID) throws SQLException {
        String actual = shippingAddressTable.getColumnValueByPrimaryID(shippingAddressID, "phoneNumber1");
        String expected = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject("shipping_address").get("phone_1").toString().replace("\"", "");
        return verifyExpectedResults(DataUtils.decryptForSandbox(actual), expected);
    }

    private String checkPostalCode(int shippingAddressID) throws SQLException {
        String actual = shippingAddressTable.getColumnValueByPrimaryID(shippingAddressID, "postalCode");
        String expected = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject("shipping_address").get("zip").toString().replace("\"", "");
        return verifyExpectedResults(DataUtils.decryptForSandbox(actual), expected);
    }

    private String checkRegion(int shippingAddressID) throws SQLException {
        String actual = shippingAddressTable.getColumnValueByPrimaryID(shippingAddressID, "region");
        String expected = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject("shipping_address").get("state").toString().replace("\"", "");
        return verifyExpectedResults(actual, expected);
    }
}
