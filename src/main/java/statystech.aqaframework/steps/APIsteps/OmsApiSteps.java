package statystech.aqaframework.steps.APIsteps;

import okhttp3.*;
import org.junit.jupiter.api.TestInfo;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.OmsTestContext;
import statystech.aqaframework.utils.ApiRestUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;

public class OmsApiSteps {

    public String sendPostRequestAndSaveResponseToContext(String jsonFileName, TestInfo testInfo) throws IOException {
        OmsTestContext omsTestContext = Context.getTestContext(testInfo.getTestMethod().get().getName(), OmsTestContext.class);
        String response = new ApiRestUtils().submitWebsiteOrder(jsonFileName);
        if (response.isEmpty()){
            return "\nResponse body is empty!\n";
        } else {
            //save to context
            return "";
        }
    }
}
