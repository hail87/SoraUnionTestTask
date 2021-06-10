package statystech.aqaframework.utils;


import io.restassured.response.Response;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

    public String submitWebsiteOrderAndGetString(String jsonFileName) {
        try {
            return submitWebsiteOrder(jsonFileName).body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "There was an error during API request\n";
    }

    public okhttp3.Response submitWebsiteOrder(String jsonFileName) {
        String jsonContent = null;
        okhttp3.Response response = null;
        try {
            jsonContent = JsonUtils.getStringFromJson(jsonFileName);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonContent);
            Request request = new Request.Builder()
                    .url("https://tihrphkst3.execute-api.us-east-1.amazonaws.com/dev/api/v1/websites/orders")
                    .method("POST", body)
                    .addHeader("X-API-Key", "rfyA0vcW2aQZHJBFXlKI4HUDuDeBJctxfBBaTW60")
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}