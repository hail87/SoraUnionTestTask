package statystech.aqaframework.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.steps.DBsteps.OrdersTableSteps;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(OrdersTableSteps.class);

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

}
