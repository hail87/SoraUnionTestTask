package statystech.aqaframework.utils;


import com.google.gson.*;
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

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static Product getJsonProductWithName(String productName) {
        return TestContext.products.stream()
                .filter(p -> p.getProductName().equalsIgnoreCase(productName)).findFirst().orElse(null);
    }

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
        makeProductObjectsFromJson();
    }


    public String getJsonContentAndLoadToContext(String jsonFilename) throws IOException {
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
        for(JsonElement jsonProduct : getProducts()){
            Product product = new Gson().fromJson(jsonProduct, Product.class);
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
