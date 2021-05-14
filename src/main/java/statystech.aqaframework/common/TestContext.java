package statystech.aqaframework.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.io.IOUtils;
import org.apache.xpath.operations.Or;
import statystech.aqaframework.DataObjects.Jackson.Order;
import statystech.aqaframework.DataObjects.Jackson.OrderItem;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.DataObjects.ProductJson.ProductDto;
import statystech.aqaframework.steps.DBsteps.WarehouseOrderSteps;
import statystech.aqaframework.utils.DataUtils;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TestContext {

    public TestContext(String testMethodName) {
        setTestMethodName(testMethodName);
    }

    private ConnectionDB connectionDB;

    private String testMethodName;
    private JsonObject jsonObject;
    private String jsonString;
    private Order order;
    private List<ProductDto> productDtoList;
    private String orderAllSysID;
    private int allSysBuyerID; //orderID in JSON
    private int orderID; //orderID in DB
    private LinkedHashMap<Integer, Integer> warehouseOrders; //[warehouseOrderID, warehouseID]

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
        List<ProductDto> products = mapper.readValue(jsonString, List.class);
        setProductDtoList(products);
    }

    public Connection getConnection() throws SQLException, IOException {
        if (connectionDB == null) {
            connectionDB = new ConnectionDB();
            connectionDB.connectDB();
        }
        return connectionDB.getCurrentConnection();
    }

    public void closeConnection() throws SQLException, IOException {
        if (connectionDB != null)
            connectionDB.getCurrentConnection().close();
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
