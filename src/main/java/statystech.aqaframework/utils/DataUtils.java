package statystech.aqaframework.utils;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.lwa.common.crypto.service.impl.ColumnCryptoServiceImpl;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.MyPath;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DataUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);

    public static String getPropertyValue(String propertyFileName, String propertyName) {
        FileInputStream fis;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream(MyPath.RESOURCES_PATH.getPath() + propertyFileName);
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
            fis = new FileInputStream(MyPath.RESOURCES_PATH.getPath() + propertyFileName);
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
        saveProperty(p, new File(MyPath.RESOURCES_PATH.getPath() + "test_rail_config.properties"));
    }

    public static String getCurrentTimestamp() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.HOUR, -4);
        return sdf.format(cal.getTime());
    }

    public static String getCurrentDate() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                downloadKubeCtlLog("oms-website-api") |
                downloadKubeCtlLog("lwa-write-data") |
                downloadKubeCtlLog("lwa-etl-orders");
                //| downloadKubeCtlLog("lwa-etl-products");
    }

    public static boolean downloadKubeCtlLog(String logName) {
        ApiClient client = null;
        String logFilePath = String.format("target/logs/%s.log", logName);
        boolean areLogsDownloadedSuccessfully = false;
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
                    writter.write(line + System.lineSeparator());
                    deltaT = System.currentTimeMillis() - start;
                    areLogsDownloadedSuccessfully = true;
                }
            } catch (Exception e) {
                logger.info("\nClosing file '" + logFilePath + "'");
            }

        } catch (ApiException e) {
            logger.warn("\nCan't get logs from kubernetes\n");
        } catch (IOException e) {
            logger.error("\nTrouble with streaming logs from the pod\n");
        } finally {
            logger.info("closing the streams\n");
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return areLogsDownloadedSuccessfully;
    }

    public static String encryptForSandbox(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return new ColumnCryptoServiceImpl("dev_encryption_passphrase").encrypt(text);
    }

    public static String encryptForTest(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return new ColumnCryptoServiceImpl("test_encryption_passphrase").encrypt(text);
    }

    public static String decryptForSandbox(String text) {
        try {
            return new ColumnCryptoServiceImpl("dev_encryption_passphrase").decrypt(text);
        } catch (IllegalStateException e) {
            return "wrong decryption";
        }
    }

    public static String decryptForTest(String text) {
        try {
            return new ColumnCryptoServiceImpl("test_encryption_passphrase").decrypt(text);
        } catch (IllegalStateException e) {
            return "wrong decryption";
        }
    }

    public static String removeUnicode(String text) {
        String result = text;
        if (text.contains("®")) {
            result = text.replace("®", "%");
        }
        if (text.contains("\t")) {
            result = text.replace("\t", "%");
        } else if (text.contains("\\t")) {
            result = text.replace("\\t", "%");
        }
        if (text.contains("\\u00ae")) {
            result = text.replace("\\u00ae", "%");
        }
        return result;
    }

    public static void clearFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public static void zipFolder(String sourceDirPath, String zipFilePath) throws IOException {
        Files.deleteIfExists(Path.of(zipFilePath));
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
    }
}



