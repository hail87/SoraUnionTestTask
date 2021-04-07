package statystech.aqaframework.steps.APIsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.utils.ApiRestUtils;

public class StageOrderApiSteps {

    private static final Logger logger = LoggerFactory.getLogger(ApiRestUtils.class);

    public String triggerOrderProcessingSandBox() {
        String errorMessage = "";
        logger.info("Triggering order processing at the SandBox");
        if (!new ApiRestUtils().sendGetRequest(
                "http://8a57e667-lwasandbox-ingres-db29-1956677137.us-east-1.elb.amazonaws.com/start"))
            errorMessage = "Response code != 200";
        return errorMessage;
    }
}
