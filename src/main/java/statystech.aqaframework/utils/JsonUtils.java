package statystech.aqaframework.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OrderJackson.OrderItem;
import statystech.aqaframework.DataObjects.ProductJson.Product;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Path;
import statystech.aqaframework.common.Context.LwaTestContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static OrderItem getJsonProductWithName(String productName) throws JsonProcessingException {
        return Context.getTestContext(LwaTestContext.class).getOrder().getOrderItems().stream()
                .filter(p -> p.getProductName().equalsIgnoreCase(productName)).findFirst().orElse(null);
    }

    public static String getValueFromJSON(String node1, String node2, String node3, String key) {
        String jsonValue = "";
        try {
            jsonValue = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject(node1).getAsJsonObject(node2).getAsJsonObject(node3)
                    .get(key).toString().replace("\"", "");
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return jsonValue;
    }

    public static String getValueFromJSON(String node, String child) {
        String jsonValue = "";
        try {
            jsonValue = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonObject(node).get(child).
                    toString().replace("\"", "");
        } catch (ClassCastException e) {
            jsonValue = Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonArray(node).get(0).getAsJsonObject().get(child)
                    .toString().replace("\"", "");
        }
        return jsonValue;
    }

    public static String getValueFromJSON(String key) {
        String jsonValue = "";
        jsonValue = Context.getTestContext(LwaTestContext.class).getJsonObject().get(key).toString().replace("\"", "");
        return jsonValue;
    }

    public static String loadObjectToContextAndGetString(String jsonFilename, String testMethodName) throws IOException {
        loadJsonObjectToTestContext(getJsonObject(jsonFilename), testMethodName);
        String jsonString = getStringFromJson(jsonFilename);
        LwaTestContext lwaTestContext = Context.getTestContext(testMethodName, LwaTestContext.class);
        lwaTestContext.setJsonString(jsonString);
        lwaTestContext.makeOrderFromJson();
        return jsonString;
    }

    public String getProductsJsonObjectsAndLoadToContext(String jsonFilename, String testMethodName) throws IOException {
        String jsonString = getStringFromJson(jsonFilename);
        ObjectMapper mapper = new ObjectMapper();
        List<Product> products = mapper.readValue(jsonString, List.class);
        Context.getTestContext(testMethodName, LwaTestContext.class).setProductJsonList(products);
        return jsonString;
    }

    public String getProductJsonObjectAndLoadToContext(String jsonFilename, String testMethodName) throws IOException {
        String jsonString = getStringFromJson(jsonFilename);
        ObjectMapper mapper = new ObjectMapper();
        Product product = mapper.readValue(jsonString, Product.class);
        Context.getTestContext(testMethodName, LwaTestContext.class).setProduct(product);
        return jsonString;
    }

    public static String getStringFromJson(String jsonFilename) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(Path.JSON_PATH.getPath() + jsonFilename), StandardCharsets.UTF_8));
        String jsonString = IOUtils.toString(reader);
        reader.close();
        return jsonString.replace("\\", "\\\\");
    }

    public static JsonArray getProducts(){
        return Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonArray("order_items");
    }

    public static JsonObject getJsonObject(String jsonFilename) {
        JsonObject jsonObject = new JsonObject();

        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(Path.JSON_PATH.getPath() + jsonFilename));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            logger.error(e.toString());
        }

        return jsonObject;
    }

    public static void loadJsonObjectToTestContext(JsonObject jsonObject, String testMethodName){
        LwaTestContext lwaTestContext = new LwaTestContext(testMethodName);
        Context.getTestContext(LwaTestContext.class).setJsonObject(jsonObject);
        Context.addTestContext(lwaTestContext);
    }
}
