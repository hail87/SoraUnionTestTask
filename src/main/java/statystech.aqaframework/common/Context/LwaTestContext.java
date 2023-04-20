package statystech.aqaframework.common.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;
import statystech.aqaframework.common.ConnectionDB;

import java.sql.SQLException;
import java.util.LinkedHashMap;

@Getter
@Setter
public class LwaTestContext extends TestContext{

    public LwaTestContext(String testMethodName) {
        setTestMethodName(testMethodName);
    }

    private ConnectionDB connectionDB;

    private String testMethodName;
    private JsonObject jsonObject;
    private String jsonString;

    private int productParentID;
    private int productID;
    private String orderAllSysID;
    private int allSysBuyerID; //orderID in JSON
    private int orderID; //orderID in DB
    private int orderLineID;
    private LinkedHashMap<Integer, Integer> warehouseOrders; //[warehouseOrderID, warehouseID]
    private int omsShippingAddressID;
    private int omsBillingAddressID;
    private int OMSBuyerAccountLicenseID;

    private int apiOrderId;
    private int apiBuyerAccountId;
    private String apiOrderStatusCd;

    private int paymentMethodID;
    private int orderExceptionHistoryID;
    private int accountAddressID;

    private String aretoInternalOrderId;
    private String aretoToken;
    private String aretoAuthorizeDescription;
    private Double aretoAuthorizeAmount;
    private String aretoCaptureDescription;
    private String aretoCaptureCode;

    private okhttp3.Response parcelLineResponse;
    private String parcelLineResponseBody;
    private int parcelLineID;
    private int warehouseBatchInventoryID;
    private int productBatchId;
    private int parcelID;

    private String responseBody;
    private JsonNode lastNode;

    public int getLastWarehouseOrderID() throws SQLException {
        if (warehouseOrders == null || warehouseOrders.size() == 0) {
            throw new SQLException("\nThere is no warehouseOrders yet\n");
        } else {
            return warehouseOrders.entrySet().stream().skip(warehouseOrders.size() - 1).findFirst().get().getKey();
        }
    }

    public void addWarehouseOrders(int warehouseOrderID, int warehouseID) {
        boolean isAlreadyThere = this.warehouseOrders.entrySet().stream().anyMatch(e -> e.getKey().equals(warehouseOrderID));
        if (!isAlreadyThere)
            this.warehouseOrders.put(warehouseOrderID, warehouseID);
    }

    @Override
    public int hashCode() {
        return getTestMethodName().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        LwaTestContext lwaTestContext = (LwaTestContext) object;
        return this.testMethodName.equalsIgnoreCase(lwaTestContext.testMethodName);
    }

}
