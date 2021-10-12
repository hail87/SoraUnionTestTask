package statystech.aqaframework.utils;


import io.restassured.response.Response;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class ApiRestUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApiRestUtils.class);

    /**
     * Authorization provided from user with userId = 7 (WarehouseManager for WareHouse 6)
     */
    public static String getWarehouseOrders() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n}");
        Request request = new Request.Builder()
                .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/warehouse-orders/search")
                .method("POST", body)
                .addHeader("Authorization", "eyJraWQiOiJ0NWNKK0hsY1BcL3JZbFI5ZTVWM3NnT2E0bW9rOU1ERUFRa1B3clBMTWhhUT0iLCJhbGciOiJSUzI1NiJ9.eyJjb2duaXRvOnJvbGVzIjpbImFybjphd3M6aWFtOjozMjY3MjYxNDIyMzk6cm9sZVwvQ29nbml0b191cGxvYWR0b3MzdGVzdEF1dGhfUm9sZSJdLCJzdWIiOiI1ZDFmMGUzYS04ZTVmLTQ1ZTktYTFhNy02OTgwYWY5OWI1NDUiLCJhdWQiOiJjNmVhdjh1YmZvNjEwMjc2Ym0wMGtkNm84IiwiY29nbml0bzpncm91cHMiOlsidGVzdCJdLCJldmVudF9pZCI6ImExMTVhZmM3LWJjYzgtNDMwNi1iZjA1LWFhY2E2OTcyZjk0MyIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjMzNTMwOTEyLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuY2EtY2VudHJhbC0xLmFtYXpvbmF3cy5jb21cL2NhLWNlbnRyYWwtMV9qNEdJa2JXMjYiLCJjb2duaXRvOnVzZXJuYW1lIjoidGVzdF93aG0iLCJleHAiOjE2MzM1MzI3MTIsImlhdCI6MTYzMzUzMDkxMn0.PShvXPFzXNhC_lZzdo65ph8a6dae6j3P-YWZ1kWdGbe-1Y7YSOa_SUhqnKy0-loVTk8xCVjDNmh0T2bPBY2M3S-OcZoP4-AaRxE5vfLbg-2HpaS4BhdOwHf25KPQFr62uPneSUDozW-bQ_4W2mmh_IPjERB6XaaaOctk6ZBjtzDzRngpu0HbYOo7i8biz1Y2de7uD2auxx_KDFWWTOkh28jdzG4m9-bNBLoqjv6SsblcooviBMNUSn6bXTbfPXbBmMsGhQeds42jXbSwuru5Fyu3zWb2PpeJMfJs6WdDI6bW5r8AfJndpcQ-EAfHI88LHO0-1-qyBLuBuW5c5jqw7A")
                .addHeader("Content-Type", "application/json")
                .build();
        String result = "";
        try {
            result = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getWarehouseOrders(int allsysOrderId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"allsys_order_id\": " + allsysOrderId + "\n}");
        Request request = new Request.Builder()
                .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/warehouse-orders/search")
                .method("POST", body)
                .addHeader("Authorization", "eyJraWQiOiJ0NWNKK0hsY1BcL3JZbFI5ZTVWM3NnT2E0bW9rOU1ERUFRa1B3clBMTWhhUT0iLCJhbGciOiJSUzI1NiJ9.eyJjb2duaXRvOnJvbGVzIjpbImFybjphd3M6aWFtOjozMjY3MjYxNDIyMzk6cm9sZVwvQ29nbml0b191cGxvYWR0b3MzdGVzdEF1dGhfUm9sZSJdLCJzdWIiOiI1ZDFmMGUzYS04ZTVmLTQ1ZTktYTFhNy02OTgwYWY5OWI1NDUiLCJhdWQiOiJjNmVhdjh1YmZvNjEwMjc2Ym0wMGtkNm84IiwiY29nbml0bzpncm91cHMiOlsidGVzdCJdLCJldmVudF9pZCI6ImExMTVhZmM3LWJjYzgtNDMwNi1iZjA1LWFhY2E2OTcyZjk0MyIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjMzNTMwOTEyLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuY2EtY2VudHJhbC0xLmFtYXpvbmF3cy5jb21cL2NhLWNlbnRyYWwtMV9qNEdJa2JXMjYiLCJjb2duaXRvOnVzZXJuYW1lIjoidGVzdF93aG0iLCJleHAiOjE2MzM1MzI3MTIsImlhdCI6MTYzMzUzMDkxMn0.PShvXPFzXNhC_lZzdo65ph8a6dae6j3P-YWZ1kWdGbe-1Y7YSOa_SUhqnKy0-loVTk8xCVjDNmh0T2bPBY2M3S-OcZoP4-AaRxE5vfLbg-2HpaS4BhdOwHf25KPQFr62uPneSUDozW-bQ_4W2mmh_IPjERB6XaaaOctk6ZBjtzDzRngpu0HbYOo7i8biz1Y2de7uD2auxx_KDFWWTOkh28jdzG4m9-bNBLoqjv6SsblcooviBMNUSn6bXTbfPXbBmMsGhQeds42jXbSwuru5Fyu3zWb2PpeJMfJs6WdDI6bW5r8AfJndpcQ-EAfHI88LHO0-1-qyBLuBuW5c5jqw7A")
                .addHeader("Content-Type", "application/json")
                .build();
        String result = "";
        try {
            result = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

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
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return "There was an error during API request\n";
    }

    public String submitWebsiteOrderAndGetString(String jsonFileName, String apiKey) {
        try {
            return submitWebsiteOrder(jsonFileName, apiKey).body().string();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return "There was an error during API request\n";
    }

    public String submitOrderAndGetString(String jsonString) {
        try {
            return submitOrder(jsonString).body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "There was an error during API request\n";
    }

    public okhttp3.Response submitWebsiteOrder(String jsonFileName) {
        return submitOrder(JsonUtils.getStringFromJson(jsonFileName));
    }

    public okhttp3.Response submitWebsiteOrder(String jsonFileName, String apiKey) {
        return submitOrder(JsonUtils.getStringFromJson(jsonFileName), apiKey);
    }

    public okhttp3.Response submitOrder(String jsonString) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonString);
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

    public okhttp3.Response submitOrder(String jsonString, String apiKey) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonString);
            Request request = new Request.Builder()
                    .url("https://tihrphkst3.execute-api.us-east-1.amazonaws.com/dev/api/v1/websites/orders")
                    .method("POST", body)
                    .addHeader("X-API-Key", apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String authorizeAretoTransactionAndGetString(String orderId) {
        try {
            return authorizeAretoTransaction(orderId).body().string();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return "There was an error during areto authorize API request\n";
    }

    private okhttp3.Response authorizeAretoTransaction(String orderId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"amount\": 200.00,\n    \"client\": {\n        \"email\": \"marko@contoso.com\",\n        \"externalId\": \"MytestUserName1\",\n        \"ip\": \"192.168.1.1\",\n        \"forwardIp\": \"192.168.1.1\",\n        \"phone\": \"+426472368233\",\n        \"dob\": {\n            \"day\": 1,\n            \"month\": 12,\n            \"year\": 1992\n        }\n    },\n    \"currency\": \"USD\",\n    \"orderId\": \"" + orderId + "\",\n    \"payment\": {\n        \"type\": \"CARD\",\n        \"createToken\": true,\n        \"cardNumber\": \"4444333322221111\",\n        \"cardType\": \"VISA\",\n        \"cardCode\": \"123\",\n        \"cardExpiryDate\": {\n            \"month\": 1,\n            \"year\": 2023\n        },\n        \"cardHolder\": {\n            \"city\": \"Rome\",\n            \"country\": \"US\",\n            \"name\": \"John\",\n            \"street\": \"24 My big street\",\n            \"zip\": \"05212\",\n            \"surname\": \"Doe\",\n            \"state\": \"AL\"\n        }\n    },\n    \"mid\": 2,\n    \"items\": [\n        {\n            \"code\": \"XR01\",\n            \"name\": \"SD Card 128GB\",\n            \"qty\": 2\n        },\n        {\n            \"code\": \"XR01\",\n            \"name\": \"SD Card 128GB\",\n            \"qty\": 2\n        }\n    ],\n    \"finalization\": {\n        \"returnUrl\": \"/processfinal\"\n    }\n}");
        Request request = new Request.Builder()
                .url("https://api.stage.areto-payment.com/api/authorize/v2")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic a19uTEpCVFFQYW9PMG1HcVc0NVBFWVFyZ20vWi9LTHFNS1Z1S1BBM0U1R09rPTo=")
                .build();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String captureAretoTransactionAndGetString(String amount, String internalOrderId) {
        try {
            return captureAretoTransaction(amount, internalOrderId).body().string();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return "There was an error during areto capture API request\n";
    }

    private okhttp3.Response captureAretoTransaction(String amount, String internalOrderId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"amount\": " + amount + "\n}");
        Request request = new Request.Builder()
                .url("https://api.stage.areto-payment.com/api/capture/v2/" + internalOrderId)
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic a19uTEpCVFFQYW9PMG1HcVc0NVBFWVFyZ20vWi9LTHFNS1Z1S1BBM0U1R09rPTo=")
                .build();

        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}