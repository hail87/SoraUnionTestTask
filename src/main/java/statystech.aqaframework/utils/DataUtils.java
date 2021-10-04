package statystech.aqaframework.utils;

import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1APIResource;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Streams;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Path;

import java.io.*;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DataUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);

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

    public static void saveProperty(Properties p, File file) {
        try {
            FileOutputStream fr = new FileOutputStream(file);
            p.store(fr, "Properties");
            fr.close();
            System.out.println("After saving properties: " + p);
        } catch (IOException e) {
            System.err.println("No property file found:" + file);
        }
    }

    public static void saveTestRailProperty(Properties p) {
        saveProperty(p, new File(Path.RESOURCES_PATH.getPath() + "test_rail_config.properties"));
    }

    public static String getCurrentTimestamp() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.HOUR, -4);
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

    public static String convertUnicodeToAscii(String input) {
        return StringEscapeUtils.unescapeJava(input);
    }

    public static String getAlphaNumericRandom(int alphaLength, int digitsLength) {
        final String Alpha = "abcdefghijklmnopqrstuvwxyz";
        final String Numeric = "0123456789";
        SecureRandom rnd = new SecureRandom();
        int len1 = alphaLength;
        int len2 = digitsLength;
        StringBuilder sb = new StringBuilder(alphaLength + digitsLength);
        for (int i = 0; i < len1; i++)
            sb.append(Alpha.charAt(rnd.nextInt(Alpha.length())));
        for (int i = 0; i < len2; i++)
            sb.append(Numeric.charAt(rnd.nextInt(Numeric.length())));
        return sb.toString();
    }

    public static void executeShellCommand(String cmd) {
        try {
            Process process
                    = Runtime.getRuntime().exec(cmd);

            StringBuilder output = new StringBuilder();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println(
                        "**************************** The Output is ******************************");
                System.out.println(output);
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean downloadKubeCtlLogs() {
        return downloadKubeCtlLog("oms-rules-engine") |
                downloadKubeCtlLog("oms-services") |
                downloadKubeCtlLog("oms-website-api");
    }

    public static boolean downloadKubeCtlLog(String logName) {
        ApiClient client = null;
        String logFilePath = String.format("target/logs/%s.log", logName);
        //String logFilePath = String.format("/Users/HAiL/IdeaProjects/aqa/target/logs/%s.log", logName);
        boolean result = false;
        try {
            logger.info("getting API client");
            client = Config.fromConfig(KubeConfig.loadKubeConfig(new FileReader(".kube/config")));
            //client = Config.defaultClient();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("Can't find Kube config file");
        } catch (IOException e) {
        e.printStackTrace();
        logger.error("Can't get Kube ApiClient");
    }
        logger.info("setting API client");
        Configuration.setDefaultApiClient(client);
        CoreV1Api coreApi = new CoreV1Api(client);
        PodLogs logs = new PodLogs();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            V1Pod pod =
                    coreApi
                            .listNamespacedPod(
                                    "lwa-sandbox", "false", null, null, null, null, null, null, null, null, null)
                            .getItems()
                            .stream().filter(p -> p.getMetadata().getName().contains(logName)).findFirst().get();
             inputStream = logs.streamNamespacedPodLog(pod);
             bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            TimeLimiter timeLimiter = new SimpleTimeLimiter();
            String line = "";
            long deltaT = 0;
            long start = System.currentTimeMillis();

            try (BufferedWriter writter = new BufferedWriter(new FileWriter(logFilePath))) {
                while (deltaT < 8000 &&
                        (line = timeLimiter.callWithTimeout(bufferedReader::readLine, 2, TimeUnit.SECONDS, false)) != null) {
                    writter.write(line);
                    deltaT = System.currentTimeMillis() - start;
                    result = true;
                }
            } catch (Exception e) {
                logger.error("\nClosing file '" + logFilePath + "'");
            }

        } catch (ApiException e) {
            //logger.error("\nCan't get logs from kubernetes\n");
        } catch (IOException e) {
            logger.error("\nTrouble with streaming logs from the pod\n");
        }finally {
            logger.info("closing the streams\n");
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}



