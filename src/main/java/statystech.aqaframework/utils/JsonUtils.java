package statystech.aqaframework.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.Batch;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.DataObjects.Warehouse;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.DBsteps.OrdersSteps;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(OrdersSteps.class);

    public static String getValueFromJSON(String node1, String node2, String node3, String key) {
        String jsonValue = "";
        try {
            jsonValue = TestContext.JSON_OBJECT.getAsJsonObject(node1).getAsJsonObject(node2).getAsJsonObject(node3)
                    .get(key).toString().replace("\"", "");
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return jsonValue;
    }

    public static String getValueFromJSON(String node, String child) {
        String jsonValue = "";
        try {
            jsonValue = TestContext.JSON_OBJECT.getAsJsonObject(node).get(child).
                    toString().replace("\"", "");
        } catch (ClassCastException e) {
            jsonValue = TestContext.JSON_OBJECT.getAsJsonArray(node).get(0).getAsJsonObject().get(child)
                    .toString().replace("\"", "");
        }
        return jsonValue;
    }

    public static String getValueFromJSON(String key) {
        String jsonValue = "";
        jsonValue = TestContext.JSON_OBJECT.get(key).toString().replace("\"", "");

        return jsonValue;
    }

    public void loadJsonObjectToTestContext(JsonObject jsonObject){

        TestContext.JSON_OBJECT = jsonObject;
    }


    public String getJsonContent(String jsonFilename) throws IOException {
        loadJsonObjectToTestContext(getJsonObject(jsonFilename));
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/main/resources/json/" + jsonFilename), StandardCharsets.UTF_8));
        String jsonString = reader.lines().collect(Collectors.joining());
        return jsonString.replace("\\", "\\\\");
    }

    public static JsonArray getProducts(){
        return TestContext.JSON_OBJECT.getAsJsonArray("order_items");
    }

    public JsonObject getJsonObject(String jsonFilename) {
        JsonObject jsonObject = new JsonObject();

        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader("src/main/resources/json/" + jsonFilename));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            logger.error(e.toString());
        }

        return jsonObject;
    }

    public static void makeProductObjectsFromJson() {
        for(JsonElement jsonProduct : JsonUtils.getProducts()){
            JsonObject jsonObject = jsonProduct.getAsJsonObject();
            Product product = new Product();
            String productAllSysID = jsonObject.get("product_id").toString().replace("\"", "");;
            product.setProductAllSysID(Integer.parseInt(productAllSysID));
            product.setProductName(jsonObject.get("product_name").toString().replace("\"", ""));
            product.setProductSKU(jsonObject.get("SKU").toString().replace("\"", ""));
            product.setProductItemPrice(jsonObject.get("product_item_price").toString().replace("\"", ""));
            product.setProductQuantity(jsonObject.get("product_quantity").toString().replace("\"", ""));

            List<Warehouse> warehouses = new ArrayList<>();

            for(JsonElement warehouseJSON : jsonObject.getAsJsonArray("ff_centers")) {
                Warehouse warehouse = new Warehouse();
                warehouse.setId(Integer.parseInt(warehouseJSON.getAsJsonObject().get("ff_center_id").toString().replace("\"", "")));
                warehouse.setName(warehouseJSON.getAsJsonObject().get("ff_center_name").toString().replace("\"", ""));
                warehouse.setAssignedQuantity(Integer.parseInt(warehouseJSON.getAsJsonObject().get("assigned_qty").toString().replace("\"", "")));

                List<Batch> batches = new ArrayList<>();

                for (JsonElement batchJSON : warehouseJSON.getAsJsonObject().getAsJsonArray("batches")) {
                    Batch batch = new Batch();
                    batch.setQuantity(Integer.parseInt(batchJSON.getAsJsonObject().get("qty").toString().replace("\"", "")));
                    batch.setNumber(batchJSON.getAsJsonObject().get("number").toString().replace("\"", ""));
                    batch.setWarehouseID(Integer.parseInt(batchJSON.getAsJsonObject().get("ff_center_id").toString().replace("\"", "")));
                    batches.add(batch);
                }

                warehouse.setBatches(batches);
                warehouses.add(warehouse);
            }
            product.setWarehouses(warehouses);

            try{
                TestContext.products.add(product);
            } catch (NullPointerException e){
                logger.info("JSON wasn't initiated yet, initiating ...");
                TestContext.products = new ArrayList<>();
                TestContext.products.add(product);
            }
        }
    }

}
