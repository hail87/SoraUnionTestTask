package statystech.aqaframework.utils;

import io.restassured.response.Response;
import org.apache.maven.wagon.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class ApiRestUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApiRestUtils.class);

    public boolean sendGetRequest(String url) {
        Response response = given()
                .when()
                .get(url)
                .then()
                .extract().response();

        logger.info("Get request have been sent. Response status code: " + response.statusCode());
        logger.info("Status code: " + response.statusCode());
        return response.statusCode() == 200;
    }

    public void getRequest(String url) {
        Response response = given()
                .when()
                .get(url)
                .then()
                .extract().response();

        logger.info("Get request have been sent. Response status code: " + response.statusCode());
        logger.info("Status code: " + response.statusCode());
        if (response.statusCode() == 200){
            response.getBody();
        }
    }
}