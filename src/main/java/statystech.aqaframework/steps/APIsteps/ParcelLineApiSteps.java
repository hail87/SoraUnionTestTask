package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.Response;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ParcelLines.AddProductButchResponse;
import statystech.aqaframework.DataObjects.ParcelLines.ParcelLinesItem;
import statystech.aqaframework.DataObjects.ParcelLines.ParcelLinesResponse;
import statystech.aqaframework.DataObjects.ProductBatch.ProductBatchResponse;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ParcelLineApiSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(ParcelLineApiSteps.class);

    public String sendGetRequestAndSaveResponseToContext(int warehouseOrderID, String token, TestInfo testInfo) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        String responseString = new ApiRestUtils().sendGetParcelLine(warehouseOrderID, token).body().string();
        logger.info("Response from API:\n" + responseString);
        if (!responseString.contains("parcel_lineid")) {
            return String.format("\nWrong response!\nResponseString:\n'%s'\n", responseString);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            ParcelLinesResponse response = mapper.readValue(responseString, ParcelLinesResponse.class);
            List<ParcelLinesItem> parcelLinesItemList = response.getParcelLines();
            testContext.setParcelLineItems(parcelLinesItemList);
            testContext.setParcelLineID(parcelLinesItemList.get(0).getParcelLineId());
            testContext.setProductID(parcelLinesItemList.get(0).getProductId());
            testContext.setWarehouseBatchInventoryID(parcelLinesItemList.get(0).getWarehouseBatchInventoryId());
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPutRequestAndSaveResponseToContext(String authToken, int expectedStatusCode, int warehouseBatchInventoryID, TestInfo testInfo) {
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        int parcelLineID = testContext.getParcelLineID();
        Response response = new ApiRestUtils().sendPutParcelLine(parcelLineID, warehouseBatchInventoryID, authToken);
        try {
            String body = response.body().string();
            testContext.setParcelLineResponseBody(body);
            Context.updateTestContext(testContext);
            logger.info("Response body : " + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Response from API:\n" + response.code());
        if (response.code() != expectedStatusCode) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, response.code());
        } else {
            return "";
        }
    }

    public String addNewProductButch(String authToken, int productID, int freeStock, TestInfo testInfo) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        Response response = new ApiRestUtils().sendPostParcelLine(productID, freeStock, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code() != 200) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", 200, response.code());
        } else {
            ObjectMapper mapper = new ObjectMapper();
            AddProductButchResponse addProductButchResponse = mapper.readValue(response.body().string(), AddProductButchResponse.class);
            testContext.setProductBatchId(addProductButchResponse.getProductBatchId());
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPostRequestAndSaveResponseToContext(String authToken, String trackingNumber, TestInfo testInfo) {
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        int parcelLineId = testContext.getParcelLineID();
        Response response = new ApiRestUtils().sendPostExternalShipmentParcelLine(parcelLineId, trackingNumber, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code() != 200) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", 200, response.code());
        } else {
            testContext.setParcelLineResponse(response);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    @SneakyThrows
    public String sendPostAddNewProductBatchAndSaveResponseToContext(String authToken, int productID, TestInfo testInfo) {
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        Response response = new ApiRestUtils().sendPostAddNewProductBatch(productID, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code() != 200) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", 200, response.code());
        } else {
            String parcelLineResponseBody = response.body().string();
            testContext.setParcelLineResponseBody(parcelLineResponseBody);
            ObjectMapper mapper = new ObjectMapper();
            ProductBatchResponse productButchResponse = mapper.readValue(parcelLineResponseBody, ProductBatchResponse.class);
            testContext.setProductBatchId(productButchResponse.getProductBatchId());
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPostCreateParcelAndSaveResponseToContext(int warehouseOrderID, String authToken, TestInfo testInfo) throws IOException {
        return sendPostCreateParcelAndSaveResponseToContext(warehouseOrderID, authToken, testInfo, 200);
    }

    public String sendPostCreateParcelAndSaveResponseToContext(int warehouseOrderID, String authToken, TestInfo testInfo, int expectedStatusCode) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        int parcelLineId = testContext.getParcelLineID();
        Response response = new ApiRestUtils().sendPostCreateParcel(parcelLineId, warehouseOrderID, authToken);
        int responseCode = response.code();
        logger.info("Response from API:\n" + responseCode);
        String body = Objects.requireNonNull(response.body()).string();
        testContext.setParcelLineResponseBody(body);
        if (responseCode != expectedStatusCode) {
            return String.format("\n%s\nWrong response status code! Expected [%d], but found [%d]", body, expectedStatusCode, response.code());
        } else if (expectedStatusCode == 400) {
            return "";
        } else {
            int parcelID = Integer.parseInt(body.replaceAll("\\D+", ""));
            testContext.setParcelID(parcelID);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPostCreateParcelsAndSaveResponseToContext(int warehouseOrderID, String authToken, TestInfo testInfo, int expectedStatusCode) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        Response response = new ApiRestUtils().sendPostCreateParcels(testContext.getParcelLineItems(), warehouseOrderID, authToken);
        int responseCode = response.code();
        logger.info("Response from API:\n" + responseCode);
        String body = Objects.requireNonNull(response.body()).string();
        testContext.setParcelLineResponseBody(body);
        if (responseCode != expectedStatusCode) {
            return String.format("\n%s\nWrong response status code! Expected [%d], but found [%d]", body, expectedStatusCode, response.code());
        } else if (expectedStatusCode == 400) {
            return "";
        } else {
            int parcelID = Integer.parseInt(body.replaceAll("\\D+", ""));
            testContext.setParcelID(parcelID);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPostRequestExternalShipmentAndSaveResponseToContext(String authToken, String trackingNumber, TestInfo testInfo) {
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        int parcelId = testContext.getParcelID();
        Response response = new ApiRestUtils().sendPostExternalShipmentParcelLine(parcelId, trackingNumber, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code() != 200) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", 200, response.code());
        } else {
            testContext.setParcelLineResponse(response);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPostStartFulfillment(int warehouseOrderId, String authToken) {
        Response response = new ApiRestUtils().sendPostStartFulfillmentParcelLine(warehouseOrderId, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code() != 200) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", 200, response.code());
        } else {
            return "";
        }
    }
}
