package statystech.aqaframework.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.OmsDto.OmsSubmitOrderJson;
import statystech.aqaframework.DataObjects.OrderJackson.OrderItem;
import statystech.aqaframework.DataObjects.ProductJson.Product;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.TestContext;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.common.MyPath;
import statystech.aqaframework.common.Context.LwaTestContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

//    public static String loadObjectToUiContextAndGetString(String jsonFilename, String testMethodName) throws IOException {
//        loadJsonObjectToUiTestContext(getJsonObject(jsonFilename), testMethodName);
//        String jsonString = getStringFromJson(jsonFilename);
//        UiTestContext uiTestContext = Context.getTestContext(testMethodName, UiTestContext.class);
//        uiTestContext.setJsonString(jsonString);
//        uiTestContext.makeOrderFromJson();
//        return jsonString;
//    }

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
        LwaTestContext lwaTestContext = Context.getTestContext(testMethodName, LwaTestContext.class);
        lwaTestContext.setProduct(product);
        Context.updateTestContext(lwaTestContext);
        return jsonString;
    }

    public static String getStringFromJson(String jsonFilename) {
        BufferedReader reader;
        String jsonString = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(MyPath.JSON_PATH.getPath() + jsonFilename), StandardCharsets.UTF_8));
            jsonString = IOUtils.toString(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString.replace("\\", "\\\\");
    }

    public static JsonArray getProducts(){
        return Context.getTestContext(LwaTestContext.class).getJsonObject().getAsJsonArray("order_items");
    }

    public static JsonObject getJsonObject(String jsonFilename) {
        JsonObject jsonObject = new JsonObject();

        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(MyPath.JSON_PATH.getPath() + jsonFilename));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            logger.error(e.toString());
        }

        return jsonObject;
    }

    public static JsonNode getJsonNode(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(jsonString);
        } catch (JsonParseException | IOException e) {
            logger.error(e.toString());
        }
        assertNotNull(actualObj);
        return actualObj;
    }

    public static String serializeJsonObjectToJsonString(OmsSubmitOrderJson omsSubmitOrderJson) {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(writer, omsSubmitOrderJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static void loadJsonObjectToTestContext(JsonObject jsonObject, String testMethodName){
        LwaTestContext lwaTestContext = Context.getTestContext(testMethodName, LwaTestContext.class);
        lwaTestContext.setJsonObject(jsonObject);
        Context.updateTestContext(lwaTestContext);
    }

    public static void loadJsonObjectToUiTestContext(JsonObject jsonObject, String testMethodName){
        TestContext testContext = Context.getTestContext(testMethodName, UiTestContext.class);
        testContext.setJsonObject(jsonObject);
        Context.updateTestContext(testContext);
    }

    public JsonNode getNode(String jsonString, String stringPath) {
        return getValue(getJsonNode(jsonString), getPath(stringPath));
    }

    public JsonNode getValue(String jsonString, String stringPath) {
        return getValue(getJsonNode(jsonString), getPath(stringPath));
    }

    public JsonNode getValue(JsonNode jsonNode, String stringPath) {
        return getValue(jsonNode, getPath(stringPath));
    }

    private List<String> getPath(String stringPath) {
        return new ArrayList<>(List.of(stringPath.split("\\.")));
    }

    private JsonNode getValue(JsonNode jsonNode, List<String> path) {
        if (path.size() == 1) {
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode
                ) {
                    if (node.get(path.get(0)) != null) {
                        return node.get(path.get(0));
                    }
                }
                return null;
            } else {
                return jsonNode.get(path.get(0));
            }
        } else {
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode
                ) {
                    if (node.get(path.get(0)) != null) {
                        return getValue(node.get(path.remove(0)), path);
                    }
                }
                return null;
            } else {
                return getValue(jsonNode.get(path.remove(0)), path);
            }
        }
    }
}
