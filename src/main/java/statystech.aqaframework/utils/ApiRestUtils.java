package statystech.aqaframework.utils;


import io.restassured.response.Response;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ParcelLines.ParcelLinesItem;

import java.io.IOException;
import java.util.List;
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
                .url(DataUtils.getPropertyValue("url.properties", "warehouseOrderSearch"))
                .method("POST", body)
                .addHeader("Authorization", DataUtils.getPropertyValue("tokens.properties", "authorizationToken"))
                .addHeader("Content-Type", "application/json")
                .build();
        String result = "";
        try {
            result = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("\nResponse from API: \n" + result);
        return result;
    }

    public static String getWarehouseOrdersNonWHMuser() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n}");
        Request request = new Request.Builder()
                .url(DataUtils.getPropertyValue("url.properties", "warehouseOrderSearch"))
                .method("POST", body)
                .addHeader("Authorization", DataUtils.getPropertyValue("tokens.properties", "nonWHMuser"))
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
                .url(DataUtils.getPropertyValue("url.properties", "warehouseOrderSearch"))
                .method("POST", body)
                .addHeader("Authorization", DataUtils.getPropertyValue("tokens.properties", "authorizationToken"))
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

    public static String getWarehouseOrders(int allsysOrderId, String region1, String region2) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format("{\n    \"allsys_order_id\": %d,\n    \"destination\": [\n        {\n            \"region\": \"%s\"\n        },\n        {\n            \"region\": \"%s\"\n        }\n    ]\n}", allsysOrderId, region1, region2));
        Request request = new Request.Builder()
                .url(DataUtils.getPropertyValue("url.properties", "warehouseOrderSearch"))
                .method("POST", body)
                .addHeader("Authorization", DataUtils.getPropertyValue("tokens.properties", "authorizationToken"))
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

    public static String getWarehouseOrders(String region1) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format("{\n    \"allsys_order_id\": null,\n    \"destination\": [\n        {\n            \"region\": \"%s\"\n        }\n    ]\n}", region1));
        Request request = new Request.Builder()
                .url(DataUtils.getPropertyValue("url.properties", "warehouseOrderSearch"))
                .method("POST", body)
                .addHeader("Authorization", DataUtils.getPropertyValue("tokens.properties", "authorizationToken"))
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

    public static String getWarehouseOrders(String region1, String region2) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format("{\n    \"allsys_order_id\": null,\n    \"destination\": [\n        {\n            \"region\": \"%s\"\n        },\n        {\n            \"region\": \"%s\"\n        }\n    ]\n}", region1, region2));
        Request request = new Request.Builder()
                .url(DataUtils.getPropertyValue("url.properties", "warehouseOrderSearch"))
                .method("POST", body)
                .addHeader("Authorization", DataUtils.getPropertyValue("tokens.properties", "authorizationToken"))
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

    public static String getWarehouseOrdersByWarehouseID(int warehouseID) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format("{\n    \"allsys_order_id\": null,\n    \"warehouse_id\": %d,\n    \"start_date\": null,\n    \"end_date\": null,\n    \"destination\":\n    [\n        {\n            \"region\": \"USA\"\n        },\n        {\n            \"region\": \"EU\"\n        },\n        {\n            \"region\": \"ROTW\"\n        }\n    ]\n}", warehouseID));
        Request request = new Request.Builder()
                .url(DataUtils.getPropertyValue("url.properties", "warehouseOrderSearch"))
                .method("POST", body)
                .addHeader("Authorization", DataUtils.getPropertyValue("tokens.properties", "authorizationToken"))
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

    public static String getWarehouseOrdersByDate(String startDate, String endDate) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format("{\n    \"allsys_order_id\": null,\n    \"warehouse_id\": null,\n    \"start_date\": \"%s\",\n    \"end_date\": \"%s\",\n    \"destination\":\n    [\n        {\n            \"region\": \"USA\"\n        },\n        {\n            \"region\": \"EU\"\n        },\n        {\n            \"region\": \"ROTW\"\n        }\n    ]\n}", startDate, endDate));
        Request request = new Request.Builder()
                .url(DataUtils.getPropertyValue("url.properties", "warehouseOrderSearch"))
                .method("POST", body)
                .addHeader("Authorization", DataUtils.getPropertyValue("tokens.properties", "authorizationToken"))
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

    public int sendGetRequest(String url) {
        return getRequest(url).statusCode();
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
            okhttp3.Response response = submitWebsiteOrder(jsonFileName);
            logger.info("\nResponse status code :\n" + response.code());
            return response.body().string();
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
                    .url(DataUtils.getPropertyValue("url.properties", "submitOrder"))
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
                    .url(DataUtils.getPropertyValue("url.properties", "submitOrder"))
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

    public okhttp3.Response sendGetParcelLine(int warehouseOrderID, String token) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(DataUtils.getPropertyValue("url.properties", "parcelLine") + "?warehouse_order_id=" + warehouseOrderID)
                    .method("GET", null)
                    .addHeader("Authorization", token)
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response sendGetParcelLine(String parcelLineId, String token) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(DataUtils.getPropertyValue("url.properties", "parcelLine") + "/" + parcelLineId + "/batch-numbers")
                    .method("GET", null)
                    .addHeader("Authorization", token)
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response sendGetWebsites(String token) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(DataUtils.getPropertyValue("url.properties", "websitesGet"))
                    .method("GET", null)
                    .addHeader("Authorization", token)
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response sendPutParcelLine(int parcelLineID, int warehouseBatchInventoryId, String authToken) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.format("{\n    \"warehouse_batch_inventory_id\": %d\n}", warehouseBatchInventoryId));
            Request request = new Request.Builder()
                    .url(DataUtils.getPropertyValue("url.properties", "parcelLine") + "/" + parcelLineID)
                    .method("PUT", body)
                    .addHeader("Authorization", authToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response sendPutOrderLine(int orderLineID, int quantity, String authToken) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"quantity\": " + quantity + "\n}");
            Request request = new Request.Builder()
                    .url(StringUtils.replace(DataUtils.getPropertyValue("url.properties", "orderLinePUT"), "{order_line_id}", String.valueOf(orderLineID)))
                    .method("PUT", body)
                    .addHeader("Authorization", authToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response sendPostCreateParcel(int parcelLineID, int warehouseOrderId, String authToken) throws IOException {
        okhttp3.Response response;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format("{\n\"warehouse_order_id\": %d,\n\"parcel_lineid_list\": [\n{\n\"parcel_lineid\": %d\n\n\n\n}\n]\n}", warehouseOrderId, parcelLineID));
        Request request = new Request.Builder()
                .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/parcels")
                .method("POST", body)
                .addHeader("Authorization", authToken)
                .addHeader("Content-Type", "application/json")
                .build();

        response = client.newCall(request).execute();
        return response;
    }

    public okhttp3.Response sendPostCreateParcels(List<ParcelLinesItem> parcelLinesItemList, int warehouseOrderId, String authToken) throws IOException {
        okhttp3.Response response;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        StringBuilder stringBuilder = new StringBuilder();
        parcelLinesItemList.forEach(parcelLinesItem -> stringBuilder.append(String.format("{\"parcel_lineid\":%d},", parcelLinesItem.getParcelLineId())));
        stringBuilder.setLength(stringBuilder.length() - 1);
        RequestBody body = RequestBody.create(mediaType, String.format("{\"warehouse_order_id\":%d,\"parcel_lineid_list\":[%s]}", warehouseOrderId, stringBuilder));
        Request request = new Request.Builder()
                .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/parcels")
                .method("POST", body)
                .addHeader("Authorization", authToken)
                .addHeader("Content-Type", "application/json")
                .build();

        response = client.newCall(request).execute();
        return response;
    }

    public okhttp3.Response sendPostParcelLine(int productID, int freeStock, String authToken) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"warehouse_id\": 6,\n    \"batch_number\": \"FRI19112021\",\n    \"batch_exp_date\": \"2023-06-30\",\n    \"quantity\": " + freeStock + ",\n    \"warehouse_product_price\": 1,\n    \"note\": \"\"\n}");
            Request request = new Request.Builder()
                    .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/products/" + productID + "/product-batches")
                    .method("POST", body)
                    .addHeader("Authorization", authToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

//    public okhttp3.Response sendPostExternalShipmentParcelLine(int parcelID, String authToken) {
//        return sendPostExternalShipmentParcelLine(parcelID, authToken, "AA2182814AA");
//    }

    public okhttp3.Response sendPostExternalShipmentParcelLine(int parcelID, String trackingNumber, String authToken) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.format("{\n    \"tracking_number\": \"%s\",\n    \"shipping_rate\": 10,\n    \"shipping_rate_currency\": \"EUR\",\n    \"is_local_pickup\": false\n}", trackingNumber));
            Request request = new Request.Builder()
                    .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/parcels/" + parcelID + "/external-shipment")
                    .method("POST", body)
                    .addHeader("Authorization", authToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response sendPostAddNewProductBatch(int productID, String authToken) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"warehouse_id\": 10,\n    \"batch_number\": \"TUI23112021\",\n    \"batch_exp_date\": \"2024-06-30\",\n    \"quantity\": 2,\n    \"warehouse_product_price\": 1,\n    \"note\": \"\"\n}");
            Request request = new Request.Builder()
                    .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/products/" + productID + "/product-batches")
                    .method("POST", body)
                    .addHeader("Authorization", authToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response sendPostAddNewProductBatch(int productID, int warehouseId, String authToken) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"warehouse_id\": " + warehouseId + ",\n    \"batch_number\": \"TUI23112021\",\n    \"batch_exp_date\": \"2024-06-30\",\n    \"quantity\": 2,\n    \"warehouse_product_price\": 1,\n    \"note\": \"\"\n}");
            Request request = new Request.Builder()
                    .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/products/" + productID + "/product-batches")
                    .method("POST", body)
                    .addHeader("Authorization", authToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response sendPostAddNewProductBatch(int productID, int warehouseId, String date, String authToken) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"warehouse_id\": " + warehouseId + ",\n    \"batch_number\": \"TUI23112021\",\n    \"batch_exp_date\": \"" + date + "\",\n    \"quantity\": 2,\n    \"warehouse_product_price\": 1,\n    \"note\": \"\"\n}");
            Request request = new Request.Builder()
                    .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/products/" + productID + "/product-batches")
                    .method("POST", body)
                    .addHeader("Authorization", authToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response sendPostStartFulfillmentParcelLine(int warehouseOrderID, String authToken) {
        okhttp3.Response response = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/warehouse-orders/" + warehouseOrderID + "/start-fulfillment")
                    .method("POST", body)
                    .addHeader("Authorization", authToken)
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
                .url(DataUtils.getPropertyValue("url.properties", "aretoAuthorize"))
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
                .url(DataUtils.getPropertyValue("url.properties", "aretoCapture") + internalOrderId)
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

    public okhttp3.Response searchProduct(String productName, String authToken) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.format("{\n    \"product_name\": \"%s\"\n}", productName));
        Request request = new Request.Builder()
                .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/products/search")
                .method("POST", body)
                .addHeader("Authorization", authToken)
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response partialSearchProduct(String productName, String authToken) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n   \"product_name\": \"" + productName + "\",\n   \"product_ids\": [],\n   \"max_results\": 10\n}");
        Request request = new Request.Builder()
                .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/products/search-partial")
                .method("POST", body)
                .addHeader("Authorization", authToken)
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response partialSearchProduct(String productName, String excludedProductIds, String authToken) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n   \"product_name\": \"" + productName + "\",\n   \"product_ids\": [" + excludedProductIds + "],\n   \"max_results\": 10\n}");
        Request request = new Request.Builder()
                .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/products/search-partial")
                .method("POST", body)
                .addHeader("Authorization", authToken)
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public okhttp3.Response getProductDetails(int productId, String authToken) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://fs6wjwxd00.execute-api.us-east-1.amazonaws.com/dev/api/v1/products/" + productId)
                .method("GET", null)
                .addHeader("Authorization", authToken)
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