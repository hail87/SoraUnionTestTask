package statystech.aqaframework.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.maven.wagon.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Path;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class ApiRestUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApiRestUtils.class);

    public boolean sendGetRequest(String url) {
        return getRequest(url).statusCode() == 200;
    }

    public Response getRequest(String url) {
        Response response = given()
                .when()
                .get(url)
                .then()
                .extract().response();
        return response;
    }

    public String submitWebsiteOrder(String jsonFileName) throws IOException {
        String jsonContent = JsonUtils.getStringFromJson(jsonFileName);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonContent);
        Request request = new Request.Builder()
                .url("https://tihrphkst3.execute-api.us-east-1.amazonaws.com/test/api/v1/websites/orders")
                .method("POST", body)
                .addHeader("X-API-Key", "ADPF569tmjFugCsfy0sm6mhJigFc2mF3TCw89s24")
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        return response.body().string();
    }
}