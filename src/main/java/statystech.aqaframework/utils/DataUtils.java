package statystech.aqaframework.utils;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

public class DataUtils {

    public String getPropertyValue(String propertyFileName, String propertyName) throws IOException {
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

    public String getJsonContent(String jsonFilename) throws IOException {
        Gson gson = new Gson();
        Object object = gson.fromJson(new FileReader("src/main/resources/json/" + jsonFilename), Object.class);
        return object.toString();
    }

}
