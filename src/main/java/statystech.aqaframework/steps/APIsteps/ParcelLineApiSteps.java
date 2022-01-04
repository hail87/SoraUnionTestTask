package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.ParcelLines.AddProductButchResponse;
import statystech.aqaframework.DataObjects.ParcelLines.ParcelLinesResponse;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.ApiRestUtils;

import java.io.IOException;

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
            testContext.setParcelLineID(response.getParcelLines().get(0).getParcelLineId());
            testContext.setWarehouseBatchInventoryID(response.getParcelLines().get(0).getWarehouseBatchInventoryId());
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPutRequestAndSaveResponseToContext(String authToken, int expectedStatusCode, int warehouseBatchInventoryID, TestInfo testInfo){
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        int parcelLineID = testContext.getParcelLineID();
        Response response = new ApiRestUtils().sendPutParcelLine(parcelLineID,warehouseBatchInventoryID, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code()!=expectedStatusCode) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", expectedStatusCode, response.code());
        } else {
            testContext.setParcelLineResponse(response);
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String addNewProductButch(String authToken, int productID, int freeStock, TestInfo testInfo) throws IOException {
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        Response response = new ApiRestUtils().sendPostParcelLine(productID, freeStock, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code()!=200) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", 200, response.code());
        } else {
            ObjectMapper mapper = new ObjectMapper();
            AddProductButchResponse addProductButchResponse = mapper.readValue(response.body().string(), AddProductButchResponse.class);
            testContext.setProductBatchId(addProductButchResponse.getProductBatchId());
            Context.updateTestContext(testContext);
            return "";
        }
    }

    public String sendPostRequestExternalShipmentAndSaveResponseToContext(String authToken, TestInfo testInfo){
        LwaTestContext testContext = Context.getTestContext(testInfo, LwaTestContext.class);
        int parcelLineId = testContext.getParcelLineID();
        Response response = new ApiRestUtils().sendPostExternalShipmentParcelLine(parcelLineId, authToken);
        logger.info("Response from API:\n" + response.code());
        if (response.code()!=200) {
            return String.format("\nWrong response status code! Expected [%d], but found [%d]", 200, response.code());
        } else {
            testContext.setParcelLineResponse(response);
            Context.updateTestContext(testContext);
            return "";
        }
    }
}
