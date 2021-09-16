package statystech.aqaframework.tests;

import io.kubernetes.client.openapi.ApiException;
import org.junit.jupiter.api.Test;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;

public class TestTestClass {

    @Test
    public void downloadAppLogs() throws IOException, ApiException {
        //DataUtils.executeShellCommand("kubectl logs service/service-oms-rules-engine -n lwa-sandbox > target/logs/oms-rules-engine.log");
        DataUtils.downloadKubeCtlLogs();
    }

    //    @Test
//    public void closeTestRailOpenRuns() {
//        new TestRailSteps().closeAllOpenTestRuns();
//    }
}
