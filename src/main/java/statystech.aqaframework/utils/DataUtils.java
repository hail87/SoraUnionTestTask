package statystech.aqaframework.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

import com.google.gson.*;
import groovy.json.StringEscapeUtils;

public class DataUtils {

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

    public String getJsonContent(String jsonFilename) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/main/resources/json/" + jsonFilename), StandardCharsets.UTF_8));
        String jsonString = reader.lines().collect(Collectors.joining());
        return jsonString.replace("\\", "\\\\");
    }

}
