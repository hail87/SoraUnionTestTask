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
            logger.info(String.format("\njsonValue '%s' and tableValue '%s' are the same\n", expectedResult, actualResult));
            return "";
        } else {
            String message = "\njsonValue and tableValue are NOT the same!\nActual (DB): '"
                    + actualResult + "'\nExpected (JSON): '" + expectedResult + "'\n";
            logger.error(message);
            return message;
        }
    }

    public String verifyExpectedResults(String actualResult, String expectedResult) {
        if (actualResult.equalsIgnoreCase(expectedResult)) {
            logger.info(String.format("\njsonValue '%s' and tableValue '%s' are the same\n", expectedResult, actualResult));
            return "";
        } else {
            String message = "\njsonValue and tableValue are NOT the same!\nActual (DB): '"
                    + actualResult + "'\nExpected (JSON): '" + expectedResult + "'\n";
            logger.info(message);
            return message;
        }
    }

}
