package statystech.aqaframework.steps.DBsteps;

import statystech.aqaframework.TableObjects.ordersTable;
import statystech.aqaframework.TableObjects.shippingAddressTable;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class ShippingAddressSteps extends Steps {

    public String checkShippingAddress() throws SQLException {
        shippingAddressTable shippingAddressTable = new shippingAddressTable();
        StringBuilder errorMessage = new StringBuilder();
        int shippingAddressID = new ordersTable().getShippingAddressIDValue();
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

        return errorMessage.toString();
    }
}
