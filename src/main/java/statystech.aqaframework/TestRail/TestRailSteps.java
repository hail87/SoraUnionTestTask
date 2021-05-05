package statystech.aqaframework.TestRail;

import com.codepine.api.testrail.model.Run;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRailSteps {

    private static final Logger logger = LoggerFactory.getLogger(TestRailSteps.class);

    public void closeAllOpenTestRuns(){
        TestRailAPI testRailAPI = new TestRailAPI();
        testRailAPI.connect();
        for (Run run : testRailAPI.getAllOpenTestRuns()){
            if (testRailAPI.closeOpenTestRun(run.getId())){
                logger.info(String.format("Test Run #%d have been close", run.getId()));
            } else {
                logger.error(String.format("Test Run #%d haven't been close", run.getId()));
            }
        }
    }

}
