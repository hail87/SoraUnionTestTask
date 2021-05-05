package statystech.aqaframework.common;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;
import statystech.aqaframework.DataObjects.Order;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.DataObjects.ProductJson.ProductDto;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TestContext {

    public TestContext(int testID){
        setId(testID);
    }

    @Getter
    @Setter
    private int id;

    public static JsonObject JSON_OBJECT;
    public static Order order;
    public static List<Product> products;
    @Setter
    @Getter
    private List<ProductDto> productDtoList;
    public static String orderAllSysID;
    public static int allSysBuyerID; //orderID in JSON
    public static int orderID; //orderID in DB
    public static LinkedHashMap<Integer, Integer> warehouseOrders; //[warehouseOrderID, warehouseID]

    public static int getLastWarehouseOrderID() {
        return warehouseOrders.entrySet().stream().skip(warehouseOrders.size() - 1).findFirst().get().getValue();
    }

    public static void cleanContext(){
        JSON_OBJECT = new JsonObject();
        order = new Order();
        products = new ArrayList<>();
        orderAllSysID = "";
        allSysBuyerID = 0;
        orderID = 0;
        warehouseOrders = new LinkedHashMap<>();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        TestContext testContext = (TestContext) object;
        return this.id == testContext.id;
    }
}
