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
        //Gson gson = new GsonBuilder().serializeNulls().create();
//        Object object = gson.fromJson(new FileReader("src/main/resources/json/" + jsonFilename), Object.class);
//        String output = gson.toJson(object).replaceAll("[^\\x00-\\x7F]",StringEscapeUtils.escapeJava("\\u00ae"));
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/main/resources/json/" + jsonFilename), StandardCharsets.UTF_8));
        return reader.lines().collect(Collectors.joining());
    }

}
