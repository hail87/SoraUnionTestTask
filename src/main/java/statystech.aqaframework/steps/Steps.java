package statystech.aqaframework.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;

import java.util.Map;

public abstract class Steps {

    private static final Logger logger = LoggerFactory.getLogger(Steps.class);

    public static String verifyExpectedResults(int ar, int er) {
        String actualResult = String.valueOf(ar);
        String expectedResult = String.valueOf(er);
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String callingMethod = stackTraceElements[2].getMethodName();
        String callingClass = Util.getCallingClass().getName().split("\\.")[Util.getCallingClass().getName().split("\\.").length - 1];
        if (actualResult.equalsIgnoreCase(expectedResult)) {
            logger.info(String.format("\n[%s:%s]: expected '%s' and actual '%s' results are the same\n", callingClass, callingMethod, expectedResult, actualResult));
            return "";
        } else {
            String message = String.format("\n[%s:%s]: expected '%s' and actual '%s' results are NOT the same\n", callingClass, callingMethod, expectedResult, actualResult);
            logger.error(message);
            return message;
        }
    }

    public String verifyExpectedResults(Map<String, String> actualAndExpected) {
        String expectedResult = actualAndExpected.get("jsonValue");
        String actualResult = actualAndExpected.get("tableValue");
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String callingMethod = stackTraceElements[2].getMethodName();
        String callingClass = Util.getCallingClass().getName().split("\\.")[Util.getCallingClass().getName().split("\\.").length - 1];
        if (actualResult.equalsIgnoreCase(expectedResult)) {
            logger.info(String.format("\n[%s:%s]: jsonValue '%s' and tableValue '%s' are the same\n", callingClass, callingMethod, expectedResult, actualResult));
            return "";
        } else {
            String message = String.format("\n[%s:%s]: jsonValue '%s' and tableValue '%s' are NOT the same\n", callingClass, callingMethod, expectedResult, actualResult);
            logger.error(message);
            return message;
        }
    }

    public String verifyExpectedResults(String actualResult, String expectedResult) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String callingMethod = stackTraceElements[2].getMethodName();
        String callingClass = Util.getCallingClass().getName().split("\\.")[Util.getCallingClass().getName().split("\\.").length - 1];
        if (actualResult.equalsIgnoreCase(expectedResult)) {
            logger.info(String.format("\n[%s:%s]: expectedResult '%s' and actualResult '%s' are the same\n", callingClass, callingMethod, expectedResult, actualResult));
            return "";
        } else {
            String message = String.format("\n[%s:%s]: expectedResult '%s' and actualResult '%s' are NOT the same\n", callingClass, callingMethod, expectedResult, actualResult);
            logger.error(message);
            return message;
        }
    }

    public String verifyExpectedResultsInt(int actualResult, int expectedResult) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String callingMethod = stackTraceElements[2].getMethodName();
        String callingClass = Util.getCallingClass().getName().split("\\.")[Util.getCallingClass().getName().split("\\.").length - 1];
        if (actualResult == expectedResult) {
            logger.info(String.format("\n[%s:%s]: expectedResult '%s' and actualResult '%s' are the same\n", callingClass, callingMethod, expectedResult, actualResult));
            return "";
        } else {
            String message = String.format("\n[%s:%s]: expectedResult '%s' and actualResult '%s' are NOT the same\n", callingClass, callingMethod, expectedResult, actualResult);
            logger.error(message);
            return message;
        }
    }


    public static void delay(int millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
