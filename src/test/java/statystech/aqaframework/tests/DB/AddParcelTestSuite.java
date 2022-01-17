package statystech.aqaframework.tests.DB;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.ParcelLineApiSteps;
import statystech.aqaframework.steps.DBsteps.OrdersSteps;
import statystech.aqaframework.steps.DBsteps.StageOrderSteps;
import statystech.aqaframework.steps.DBsteps.WarehouseOrderSteps;
import statystech.aqaframework.tests.TestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class AddParcelTestSuite extends TestClass {

    private static final Logger logger = LoggerFactory.getLogger(AddParcelTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "3418");
        DataUtils.saveTestRailProperty(properties);
        if (TestRailReportExtension.isTestRailAnnotationPresent) {
            TestRailReportExtension.reportResults();
        }
    }

    @BeforeEach
    public void setTestContext(TestInfo testInfo) throws SQLException, IOException {
        String name = testInfo.getTestMethod().get().getName();
        logger.info(String.format(
                "\nTest № %d has been started : '%s'\n", testInfo.getTestMethod().get().getAnnotation(TestRailID.class).id(), name));
        LwaTestContext lwaTestContext = new LwaTestContext(name);
        lwaTestContext.getConnection();
        Context.addTestContext(lwaTestContext);
    }

    @TestRailID(id = 17866)
    @ParameterizedTest
    @ValueSource(strings = {"GetWarehouseOrderNoCriteria3.json"})
    public void addParcelWrongUserRole(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {

        StringBuilder errorMessage = new StringBuilder();
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        logger.info("------------------------------------Precondition Step 1------------------------------------");
        int id = stageOrderSteps.insertJsonToTableAndContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 2------------------------------------");
        new OrdersSteps().setOrderIDtoContext();
        logger.info("------------------------------------Precondition Step 3------------------------------------");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        ParcelLineApiSteps parcelLineApiSteps = new ParcelLineApiSteps();
        int warehouseOrderId = new WarehouseOrderSteps().getWarehouseOrderId(lwaTestContext.getOrderID());
        logger.info("------------------------------------Precondition Step 4------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostStartFulfillment(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7")));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Precondition Step 5,6------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendGetRequestAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "WHMuser7"),
                testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        logger.info("------------------------------------Step 1------------------------------------");
        errorMessage.append(parcelLineApiSteps.sendPostCreateParcelAndSaveResponseToContext(
                warehouseOrderId,
                DataUtils.getPropertyValue("tokens.properties", "User24"),
                testInfo, 400));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        String response = lwaTestContext.getParcelLineResponse().body().string();

        logger.info("------------------------------------Response------------------------------------\n" + response);

        errorMessage.append(parcelLineApiSteps.verifyActualResultsContains(response, "User does not have permission to access the endpoint. Please contact support at"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }
}
