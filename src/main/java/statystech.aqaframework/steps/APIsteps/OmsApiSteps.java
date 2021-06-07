package statystech.aqaframework.steps.APIsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInfo;
import statystech.aqaframework.DataObjects.OmsDto.Response;
import statystech.aqaframework.DataObjects.ProductJson.Product;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.OmsTestContext;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.util.List;

public class OmsApiSteps {

    public String sendPostRequestAndSaveResponseToContext(String jsonFileName, TestInfo testInfo) throws IOException {
        OmsTestContext omsTestContext = Context.getTestContext(testInfo.getTestMethod().get().getName(), OmsTestContext.class);
        String responseString = new ApiRestUtils().submitWebsiteOrder(jsonFileName);
        if (responseString.isEmpty()){
            return "\nResponse body is empty!\n";
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Response response = mapper.readValue(responseString, Response.class);
            omsTestContext.setOrderId(response.getOrderId());
            omsTestContext.setBuyerAccountId(response.getBuyerAccountId());
            omsTestContext.setOrderStatusCd(response.getOrderStatusCd());
            omsTestContext.setResponse(response);
            return "";
        }
    }
}
