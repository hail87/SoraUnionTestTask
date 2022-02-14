package statystech.aqaframework.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;
import statystech.aqaframework.TableObjects.TableObject;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.utils.JsonUtils;

import java.util.Map;

public abstract class Steps {

    private static final Logger logger = LoggerFactory.getLogger(Steps.class);

    protected TableObject tableObject;

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

    public String verifyActualResultsContains(String actualResult, String expectedResult) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String callingMethod = stackTraceElements[2].getMethodName();
        String callingClass = Util.getCallingClass().getName().split("\\.")[Util.getCallingClass().getName().split("\\.").length - 1];
        if (actualResult.contains(expectedResult)) {
            logger.info(String.format("\n[%s:%s]: actualResult '%s' contains expectedResult '%s'\n", callingClass, callingMethod, actualResult, expectedResult));
            return "";
        } else {
            String message = String.format("\n[%s:%s]: actualResult '%s' aren't contain expectedResult '%s'\n", callingClass, callingMethod, actualResult, expectedResult);
            logger.error(message);
            return message;
        }
    }

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

    public String verifyTableIsEmpty(){
        if (tableObject.getRowsQuantity() != 0){
            return String.format("Table %s is NOT empty, rows quantity: %d", tableObject.getName(), tableObject.getRowsQuantity());
        }
        return "";
    }

    public String verifyExpectedResultsContains(String actualResult, String expectedResult) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String callingMethod = stackTraceElements[2].getMethodName();
        String callingClass = Util.getCallingClass().getName().split("\\.")[Util.getCallingClass().getName().split("\\.").length - 1];
        if (actualResult.contains(expectedResult)) {
            logger.info(String.format("\n[%s:%s]: actualResult '%s' contains expectedResult '%s'\n", callingClass, callingMethod, actualResult, expectedResult));
            return "";
        } else {
            String message = String.format("\n[%s:%s]: actualResult '%s' contains expectedResult '%s'\n", callingClass, callingMethod, actualResult, expectedResult);
            logger.error(message);
            return message;
        }
    }

    public String verifyJsonResponseContainsNotNullAttribute(String attributeStringPath, LwaTestContext lwaTestContext) {
        JsonUtils jsonUtils = new JsonUtils();
        var value = jsonUtils.getValue(lwaTestContext.getResponseBody(), attributeStringPath);
        lwaTestContext.setLastNode(value);
        Context.updateTestContext(lwaTestContext);
        if (value.isNull()) {
            return String.format("\nNoda '%s' haven't been found at the json response", attributeStringPath);
        }
        logger.info(String.format("\nNoda '%s' successfully have been found at the json response", attributeStringPath));
        return "";
    }

    public String verifyJsonResponseContainsNotNullAttribute(String attributeStringPath, int expectedValue, LwaTestContext lwaTestContext) {
        JsonUtils jsonUtils = new JsonUtils();
        var value = jsonUtils.getValue(lwaTestContext.getResponseBody(), attributeStringPath);
        lwaTestContext.setLastNode(value);
        Context.updateTestContext(lwaTestContext);
        if (value.isNull()) {
            return String.format("\nNoda '%s' haven't been found at the json response", attributeStringPath);
        }
        if (value.intValue() != expectedValue){
            return String.format("\nExpected value '%d' and Node value '%d' aren't the same", expectedValue, value.intValue());
        }
        logger.info(String.format("\nExpected value '%d' and Node value '%d' are the same", expectedValue, value.intValue()));
        return "";
    }

    public String verifyJsonResponseContainsAttribute(String attributeStringPath, LwaTestContext lwaTestContext) {
        JsonUtils jsonUtils = new JsonUtils();
        var node = jsonUtils.getNode(lwaTestContext.getResponseBody(), attributeStringPath);
        lwaTestContext.setLastNode(node);
        Context.updateTestContext(lwaTestContext);
        if (node.isNull() && !node.isValueNode()) {
            return String.format("\nNoda '%s' haven't been found at the json response", attributeStringPath);
        }
        logger.info(String.format("\nNoda '%s' successfully have been found at the json response", attributeStringPath));
        return "";
    }

}
