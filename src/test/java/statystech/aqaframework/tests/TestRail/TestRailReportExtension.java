package statystech.aqaframework.tests.TestRail;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Project;
import com.codepine.api.testrail.model.Result;
import com.codepine.api.testrail.model.ResultField;
import com.codepine.api.testrail.model.Run;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Path;
import statystech.aqaframework.utils.DataUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TestRailReportExtension implements TestWatcher, BeforeAllCallback {

    private static final Logger logger = LoggerFactory.getLogger(TestRailReportExtension.class);
    public static boolean isTestRailAnnotationPresent = false;

    private enum TestRailStatus {
        PASSED(1),
        BLOCKED(2),
        UNTESTED(3),
        RETEST(4),
        FAILED(5),
        DISABLED(6); //This is a custom test status added manually!

        private int id;

        public int getId() {
            return id;
        }

        TestRailStatus(int id) {
            this.id = id;
        }
    }

    private static boolean started = false;
    private static final String TESTRAIL_REPORT = "TEST_RAIL";
    @Setter
    private static List<Result> results = new CopyOnWriteArrayList<>();

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (!started) {
            getStore(extensionContext).put(TESTRAIL_REPORT, new CloseableOnlyOnceResource());
            started = true;
        }
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        addResult(extensionContext, TestRailStatus.DISABLED);
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        addResult(extensionContext, TestRailStatus.PASSED);
    }

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        addResult(extensionContext, TestRailStatus.RETEST);
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        addResult(extensionContext, TestRailStatus.FAILED);
    }

    public static Properties loadProperties() {
        return DataUtils.getProperty("test_rail_config.properties");
    }

    private void addResult(ExtensionContext extensionContext, TestRailStatus status) {
        isTestRailAnnotationPresent = extensionContext.getElement().isPresent() && extensionContext.getElement()
                .get().isAnnotationPresent(TestRailID.class);
        if (isTestRailAnnotationPresent) {
            TestRailID element = extensionContext.getElement().get().getAnnotation(TestRailID.class);
            Result result = new Result().setTestId(element.id())
                    .setStatusId(status.getId())
                    .setCaseId(element.id());
            addResult(result);
        }
    }

    public static void addResult(Result result) {
        results.add(result);
    }

    public static void reportResults() {
        Properties properties = loadProperties();
        final String url = properties.getProperty("testrail_url").trim();
        final String userId = properties.getProperty("testrail_userId").trim();
        final String pwd = properties.getProperty("testrail_pwd").trim();
        final String projectId = properties.getProperty("testrail_projectId").trim();
        final Integer testSuiteId = Integer.parseInt(properties.getProperty("testrail_testSuiteId").trim());; // Suite ID should be unique per project
        int runID = 0;
        try {
            runID = Integer.parseInt(DataUtils.getTestRailPropertyValue("testrail_runId"));
        } catch (NumberFormatException | IOException e) {
            logger.info("No runID set, new Test Run will be created");
        }
//        final Integer planId = 1111; // Test plan reflects current version which is tested
//        final Integer milestone = 666; // Milestone is set per each project and should reflect release version(e.g. 1.0, 666)
//        final String browserName = "browserName";
//        final String description = "Test Run description. It can contain links to build or some other useful information";
        TestRail testRail = TestRail.builder(url, userId, pwd).build();
        Project project = testRail.projects().get(Integer.parseInt(projectId)).execute();
        Run run;
        if (runID == 0) {
            run = testRail.runs()
                    .add(project.getId(),
                            new Run().setName("AQA framework report [" + DataUtils.getCurrentTimestamp() + "]")
                                    .setIncludeAll(false)
                                    .setSuiteId(testSuiteId)
//                                .setMilestoneId(milestone)
//                                .setPlanId(planId)
                                    .setCaseIds(results.stream()
                                            .map(Result::getCaseId).collect(Collectors.toCollection(CopyOnWriteArrayList::new)))
                    ).execute();
            runID = run.getId();
        }
        List<ResultField> customResultFields = testRail.resultFields().list().execute();
        testRail.results().addForCases(runID, results, customResultFields).execute();
        addLogsToTestRun(runID);
        logger.info("Closing test run #" + runID);
        testRail.runs().close(runID).execute();
    }

    private static void addLogsToTestRun(int runID){
        String url = Path.TEST_RAIL.getPath() + "index.php?/api/v2/add_attachment_to_run/" + runID;
        Properties properties = DataUtils.getProperty("test_rail_config.properties");
        final String userId = properties.getProperty("testrail_userId").trim();
        final String pwd = properties.getProperty("testrail_pwd").trim();
        logger.info("[Uploading log file] Sending post to:" + url);
        LogManager.shutdown(); //Delete to show full logs until very end, but proper log file uploading to the testRail won't be guaranteed
        String[] CMD_ARRAY = {"curl", "-H", "Content-Type: multipart/form-data", "-u", userId + ":" + pwd, "-F", "attachment=@target/logs/test.log", url};
        ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
        Process process = null;
        try {
            process = processBuilder.start();
            process.waitFor();
            logger.info(IOUtils.toString(process.getInputStream()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            try {
                logger.error(IOUtils.toString(process.getErrorStream()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        logger.info("\nPost request with logs has been sent. \nResponse status code: " + process.exitValue() + "\nBody :\n" + process.info().toString());
        process.destroy();
    }

    public static class CloseableOnlyOnceResource implements ExtensionContext.Store.CloseableResource {
        @Override
        public void close() {
            //After all tests run hook.
//            if(isTestRailAnnotationPresent) {
//                reportResults();
//            }
        }
    }
}
