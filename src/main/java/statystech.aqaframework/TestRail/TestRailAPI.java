package statystech.aqaframework.TestRail;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Run;
import statystech.aqaframework.utils.DataUtils;

import java.util.List;
import java.util.Properties;

public class TestRailAPI {

    public static Properties loadProperties() {
        return DataUtils.getProperty("test_rail_config.properties");
    }

    TestRail testRailConnection;

    public TestRail connect(){
        Properties properties = loadProperties();
        final String url = properties.getProperty("testrail_url").trim();
        final String userId = properties.getProperty("testrail_userId").trim();
        final String pwd = properties.getProperty("testrail_pwd").trim();
        testRailConnection = TestRail.builder(url, userId, pwd).build();
        return testRailConnection;
    }

    public List<Run> getAllOpenTestRuns(){
        int projectID = Integer.parseInt(DataUtils.getProperty("test_rail_config.properties").getProperty("testrail_projectId"));
        return testRailConnection.runs().list(projectID).isCompleted(false).execute();
    }

    public boolean closeOpenTestRun(int runID){
        testRailConnection.runs().close(runID).execute();
        return testRailConnection.runs().get(runID).execute().isCompleted();
    }
}
