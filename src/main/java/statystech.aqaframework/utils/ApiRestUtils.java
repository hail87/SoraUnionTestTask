package statystech.aqaframework.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class ApiRestUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApiRestUtils.class);

    public boolean sendGetRequest(String url) {
        //RestAssured.baseURI = url;
        Response response = given()
                .when()
                .get(url)
                .then()
                .extract().response();

        logger.info("Get request have been sent. Response status code: " + response.statusCode());
        logger.info("Status code: " + response.statusCode());
        return response.statusCode() == 200;
    }
}