package statystech.aqaframework.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Path;
import statystech.aqaframework.common.TestContext;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class DataUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);
    Calendar nowGMT = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar nowGMTPlus4 = Calendar.getInstance(TimeZone.getTimeZone("GMT+4"));

    public static String getPropertyValue(String propertyFileName, String propertyName) {
        FileInputStream fis;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream(Path.RESOURCES_PATH.getPath() + propertyFileName);
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
            fis = new FileInputStream(Path.RESOURCES_PATH.getPath() + propertyFileName);
            prop.load(fis);
        } catch (IOException e) {
            System.err.println("No property file found:" + propertyFileName);
        }
        return prop;
    }

    public static String getCurrentTimestamp() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.HOUR,-4);
        return sdf.format(cal.getTime());
    }

    public static void updateTestRailPropertyParameter(String parameter, String value) throws IOException {
        updatePropertyParameter("test_rail_config.properties", parameter, value);
    }

    public static String getTestRailPropertyValue(String parameter) throws IOException {
        return getPropertyValue("test_rail_config.properties", parameter);
    }

    public static void updatePropertyParameter(String propertyFileName, String parameter, String value) throws IOException {
        Properties props = getProperty(propertyFileName);
        FileOutputStream out = new FileOutputStream(propertyFileName);
        props.setProperty(parameter, value);
        props.store(out, null);
        out.close();
    }

    public static String convertUnicodeToAscii(String input){
        return StringEscapeUtils.unescapeJava(input);
    }
}
