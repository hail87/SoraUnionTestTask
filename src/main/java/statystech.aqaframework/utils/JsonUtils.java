package statystech.aqaframework.utils;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.Order;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.DataObjects.ProductJson.ProductDto;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

//    public static Product getJsonProductWithName(String productName) {
//        return TestContext.products.stream()
//                .filter(p -> p.getProductName().equalsIgnoreCase(productName)).findFirst().orElse(null);
//    }
//
//    public static String getValueFromJSON(String node1, String node2, String node3, String key) {
//        String jsonValue = "";
//        try {
//            jsonValue = Context.getTestContext().getJsonObject().getAsJsonObject(node1).getAsJsonObject(node2).getAsJsonObject(node3)
//                    .get(key).toString().replace("\"", "");
//        } catch (ClassCastException e) {
//            e.printStackTrace();
//        }
//        return jsonValue;
//    }
//
//    public static String getValueFromJSON(String node, String child) {
//        String jsonValue = "";
//        try {
//            jsonValue = Context.getTestContext().getJsonObject().getAsJsonObject(node).get(child).
//                    toString().replace("\"", "");
//        } catch (ClassCastException e) {
//            jsonValue = Context.getTestContext().getJsonObject().getAsJsonArray(node).get(0).getAsJsonObject().get(child)
//                    .toString().replace("\"", "");
//        }
//        return jsonValue;
//    }

    public static String getValueFromJSON(String key) {
        String jsonValue = "";
        //String testMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        jsonValue = Context.getTestContext().getJsonObject().get(key).toString().replace("\"", "");
        return jsonValue;
    }

    public String loadObjectToContextAndGetString(String jsonFilename, String testMethodName) throws IOException {
        loadJsonObjectToTestContext(getJsonObject(jsonFilename), testMethodName);
        String jsonString = getStringFromJson(jsonFilename);
        Context.getTestContext(testMethodName).setJsonString(jsonString);
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(
//                        new FileInputStream("src/main/resources/json/" + jsonFilename), StandardCharsets.UTF_8));
//        String jsonString = reader.lines().collect(Collectors.joining());
        return jsonString;//.replace("\\", "\\\\");
    }

    public String getProductJsonObjectsAndLoadToContext(String jsonFilename, String testMethodName) throws IOException {
        String jsonString = getStringFromJson(jsonFilename);
        ObjectMapper mapper = new ObjectMapper();
        List<ProductDto> products = mapper.readValue(jsonString, List.class);
        Context.getTestContext(testMethodName).setProductDtoList(products);
        return jsonString;
    }

    public String getStringFromJson(String jsonFilename) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/main/resources/json/" + jsonFilename), StandardCharsets.UTF_8));
        String jsonString = IOUtils.toString(reader);
        reader.close();
        return jsonString.replace("\\", "\\\\");
    }

    public static JsonArray getProducts(){
        String testMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return Context.getTestContext(testMethodName).getJsonObject().getAsJsonArray("order_items");
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

    public void loadJsonObjectToTestContext(JsonObject jsonObject, String testMethodName){
        TestContext testContext = new TestContext(testMethodName);
        Context.getTestContext().setJsonObject(jsonObject);
        Context.addTestContext(testContext);
        //makeObjectsFromJsonAndLoadToContext();
    }
}
