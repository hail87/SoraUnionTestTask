package statystech.aqaframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class DataUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    public String getPropertyValue(String propertyFileName, String propertyName) {
        FileInputStream fis;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream("src/main/resources/" + propertyFileName);
            prop.load(fis);
        } catch (IOException e) {
            System.err.println("No property file found:" + propertyFileName);
        }
        return prop.getProperty(propertyName);
    }

    public static Properties getProperty(String propertyFileName) {
        FileInputStream fis;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream("src/main/resources/" + propertyFileName);
            prop.load(fis);
        } catch (IOException e) {
            System.err.println("No property file found:" + propertyFileName);
        }
        return prop;
    }

    public static String getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);
    }

    public static void updateTestRailPropertyParameter(String parameter, String value) throws IOException {
        updatePropertyParameter("test_rail_config.properties", parameter, value);
    }

    public static void updatePropertyParameter(String propertyFileName, String parameter, String value) throws IOException {
        Properties props = getProperty(propertyFileName);
        FileOutputStream out = new FileOutputStream(propertyFileName);
        props.setProperty(parameter, value);
        props.store(out, null);
        out.close();
    }

}
