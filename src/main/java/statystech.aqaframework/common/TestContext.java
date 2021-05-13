package statystech.aqaframework.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.xpath.operations.Or;
import statystech.aqaframework.DataObjects.Jackson.Order;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.DataObjects.ProductJson.ProductDto;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
public class TestContext {

    public TestContext(String testMethodName){
        setTestMethodName(testMethodName);
    }

    private String testMethodName;
    private JsonObject jsonObject;
    private String jsonString;
    private Order order;
    //private List<Product> products;
    private List<ProductDto> productDtoList;
    private String orderAllSysID;
    private int allSysBuyerID; //orderID in JSON
    private int orderID; //orderID in DB
    private LinkedHashMap<Integer, Integer> warehouseOrders; //[warehouseOrderID, warehouseID]

    public int getLastWarehouseOrderID() {
        return warehouseOrders.entrySet().stream().skip(warehouseOrders.size() - 1).findFirst().get().getValue();
    }

//    public void cleanContext(){
//        jsonObject = new JsonObject();
//        order = new Order();
//        products = new ArrayList<>();
//        orderAllSysID = "";
//        allSysBuyerID = 0;
//        orderID = 0;
//        warehouseOrders = new LinkedHashMap<>();
//    }

    public void makeOrderFromJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(jsonString, Order.class);
        setOrder(order);
    }

    public void makeProductsDtoFromJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<ProductDto> products = mapper.readValue(jsonString, List.class);
        setProductDtoList(products);
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

        TestContext testContext = (TestContext) object;
        return this.testMethodName.equalsIgnoreCase(testContext.testMethodName);
    }
}
