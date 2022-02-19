package statystech.aqaframework.common.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;
import statystech.aqaframework.DataObjects.IrsDto.SearchProductResponse;
import statystech.aqaframework.DataObjects.OmsDto.OmsSubmitOrderJson;
import statystech.aqaframework.DataObjects.OmsDto.Response;
import statystech.aqaframework.DataObjects.OrderJackson.Order;
import statystech.aqaframework.DataObjects.OrderJackson.OrderItem;
import statystech.aqaframework.DataObjects.ParcelLines.ParcelLinesItem;
import statystech.aqaframework.DataObjects.ParcelLines.ParcelLinesResponse;
import statystech.aqaframework.DataObjects.ProductJson.Product;
import statystech.aqaframework.DataObjects.WarehouseSearch.WarehouseSearchResponse;
import statystech.aqaframework.common.ConnectionDB;
import statystech.aqaframework.utils.DataUtils;
import statystech.aqaframework.utils.JsonUtils;


import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

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
    private Order order;
    private List<Product> productJsonList;
    private Product product;
    private int productID;
    private String orderAllSysID;
    private int allSysBuyerID; //orderID in JSON
    private int orderID; //orderID in DB
    private LinkedHashMap<Integer, Integer> warehouseOrders; //[warehouseOrderID, warehouseID]
    private int omsShippingAddressID;
    private int omsBillingAddressID;
    private int OMSBuyerAccountLicenseID;

    private int apiOrderId;
    private int apiBuyerAccountId;
    private String apiOrderStatusCd;
    private Response response;
    private OmsSubmitOrderJson omsSubmitOrderJson;
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
    private ParcelLinesResponse parcelLineResponseObject;
    private int parcelLineID;
    private int warehouseBatchInventoryID;
    private List<ParcelLinesItem> parcelLineItems;
    private int productBatchId;
    private int parcelID;

    private SearchProductResponse searchProductResponse;

    private String responseBody;
    private JsonNode lastNode;

    private WarehouseSearchResponse warehouseSearchResponse;

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

    public void makeOrderFromJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(DataUtils.convertUnicodeToAscii(jsonString), Order.class);
        setOrder(order);
    }

    public Order getOrder() {
        if (order == null) {
            try {
                makeOrderFromJson();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return order;
    }

    public OrderItem getItem(String name) {
        return order.getOrderItems().stream().filter(p -> p.getProductName().equalsIgnoreCase(name)).findFirst().get();
    }

    public void makeProductsDtoFromJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Product> products = mapper.readValue(jsonString, List.class);
        setProductJsonList(products);
    }

    public void setSubmitOrderObjectsFromJson() throws JsonProcessingException {
        if(jsonString == null || jsonString.isEmpty()){
            setJsonString(jsonObject.toString());
        }
        ObjectMapper mapper = new ObjectMapper();
        OmsSubmitOrderJson omsSubmitOrderJson = mapper.readValue(DataUtils.convertUnicodeToAscii(jsonString), OmsSubmitOrderJson.class);
        setOmsSubmitOrderJson(omsSubmitOrderJson);
    }

    public void updateBuyerAccountID(){
        omsSubmitOrderJson.getBuyer().setBuyerAccountId(apiBuyerAccountId);
    }

    public void updateBuyerIpAddress(String ip){
        omsSubmitOrderJson.getBuyer().setBuyerIpAddress(ip);
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
