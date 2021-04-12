package statystech.aqaframework.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class Steps {

    private static final Logger logger = LoggerFactory.getLogger(Steps.class);

    public String verifyExpectedResults(Map<String, String> actualAndExpected) {
        String expectedResult = actualAndExpected.get("jsonValue");
        String actualResult = actualAndExpected.get("tableValue");
        if (actualResult.equalsIgnoreCase(expectedResult)) {
            logger.info(String.format("jsonValue '%s' and tableValue '%s' are the same", expectedResult, actualResult));
            return "";
        } else {
            String message = "jsonValue and tableValue are NOT the same!\nActual: '"
                    + actualResult + "'\nExpected: '" + expectedResult + "'";
            logger.info(message);
            return message;
        }
    }

    public String verifyExpectedResults(String actualResult, String expectedResult) {
        if (actualResult.equalsIgnoreCase(expectedResult)) {
            logger.info(String.format("jsonValue '%s' and tableValue '%s' are the same", expectedResult, actualResult));
            return "";
        } else {
            String message = "jsonValue and tableValue are NOT the same!\nActual: '"
                    + actualResult + "'\nExpected: '" + expectedResult + "'";
            logger.info(message);
            return message;
        }
    }

}
