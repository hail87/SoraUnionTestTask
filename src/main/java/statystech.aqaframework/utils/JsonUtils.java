package statystech.aqaframework.utils;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.Jackson.OrderItem;
import statystech.aqaframework.DataObjects.Order;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.DataObjects.ProductJson.ProductDto;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.Path;
import statystech.aqaframework.common.TestContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static OrderItem getJsonProductWithName(String productName) throws JsonProcessingException {
        return Context.getTestContext().getOrder().getOrderItems().stream()
                .filter(p -> p.getProductName().equalsIgnoreCase(productName)).findFirst().orElse(null);
    }

    public static String getValueFromJSON(String node1, String node2, String node3, String key) {
        String jsonValue = "";
        try {
            jsonValue = Context.getTestContext().getJsonObject().getAsJsonObject(node1).getAsJsonObject(node2).getAsJsonObject(node3)
                    .get(key).toString().replace("\"", "");
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return jsonValue;
    }

    public static String getValueFromJSON(String node, String child) {
        String jsonValue = "";
        try {
            jsonValue = Context.getTestContext().getJsonObject().getAsJsonObject(node).get(child).
                    toString().replace("\"", "");
        } catch (ClassCastException e) {
            jsonValue = Context.getTestContext().getJsonObject().getAsJsonArray(node).get(0).getAsJsonObject().get(child)
                    .toString().replace("\"", "");
        }
        return jsonValue;
    }

    public static String getValueFromJSON(String key) {
        String jsonValue = "";
        jsonValue = Context.getTestContext().getJsonObject().get(key).toString().replace("\"", "");
        return jsonValue;
    }

    public static String loadObjectToContextAndGetString(String jsonFilename, String testMethodName) throws IOException {
        loadJsonObjectToTestContext(getJsonObject(jsonFilename), testMethodName);
        String jsonString = getStringFromJson(jsonFilename);
        TestContext testContext = Context.getTestContext(testMethodName);
        testContext.setJsonString(jsonString);
        testContext.makeOrderFromJson();
        return jsonString;
    }

    public String getProductJsonObjectsAndLoadToContext(String jsonFilename, String testMethodName) throws IOException {
        String jsonString = getStringFromJson(jsonFilename);
        ObjectMapper mapper = new ObjectMapper();
        List<ProductDto> products = mapper.readValue(jsonString, List.class);
        Context.getTestContext(testMethodName).setProductDtoList(products);
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
        return Context.getTestContext().getJsonObject().getAsJsonArray("order_items");
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
        TestContext testContext = new TestContext(testMethodName);
        Context.getTestContext().setJsonObject(jsonObject);
        Context.addTestContext(testContext);
    }
}
