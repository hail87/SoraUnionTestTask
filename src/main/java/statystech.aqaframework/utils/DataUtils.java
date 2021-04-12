package statystech.aqaframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.TestContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DataUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);

    public String getPropertyValue(String propertyFileName, String propertyName) {
        FileInputStream fis;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream("src/main/resources/" + propertyFileName);
            prop.load(fis);
        } catch (IOException e) {
            System.err.println("No property file found:" + propertyName);
        }
        return prop.getProperty(propertyName);
    }

    public static String getValueFromJSON(String node, String key){
        String jsonValue = "";
        try {
            jsonValue = TestContext.JSON_OBJECT.getAsJsonObject(node).get(key).
                    toString().replace("\"", "");
        } catch (ClassCastException e) {
            jsonValue = TestContext.JSON_OBJECT.getAsJsonArray(node).get(0).getAsJsonObject().get(key)
                    .toString().replace("\"", "");
        }
        return jsonValue;
    }

}
