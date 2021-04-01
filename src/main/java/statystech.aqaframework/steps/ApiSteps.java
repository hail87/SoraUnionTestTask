package statystech.aqaframework.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.utils.ApiRestUtils;

public class ApiSteps {

    private static final Logger logger = LoggerFactory.getLogger(ApiRestUtils.class);

    public boolean triggerOrderProcessingSandBox(){
        logger.info("Triggering order processing at the SandBox");
        return new ApiRestUtils().sendGetRequest(
                "http://8a57e667-lwasandbox-ingres-db29-1956677137.us-east-1.elb.amazonaws.com/start");
    }


}
