package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.OmsApiSteps;
import statystech.aqaframework.steps.DBsteps.OrderExceptionHistorySteps;
import statystech.aqaframework.steps.DBsteps.OrderStatusHistorySteps;
import statystech.aqaframework.steps.DBsteps.OrdersSteps;
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
public class OrderValidationTestSuite extends TestClass {

    private static final Logger logger = LoggerFactory.getLogger(OrderValidationTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "2170");
        DataUtils.saveTestRailProperty(properties);
        if (TestRailReportExtension.isTestRailAnnotationPresent) {
            TestRailReportExtension.reportResults();
        }
    }

    @BeforeEach
    public void setTestContext(TestInfo testInfo) throws SQLException, IOException {
        String name = testInfo.getTestMethod().get().getName();
        logger.info(String.format(" Test started : %s\n", name));
        LwaTestContext lwaTestContext = new LwaTestContext(name);
        lwaTestContext.getConnection();
        Context.addTestContext(lwaTestContext);
    }

    @TestRailID(id = 7809)
    @ParameterizedTest
    @CsvSource({"submitNewOrder.json"})
    public void verifySuspiciousAccount(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        OmsApiSteps omsApiSteps = new OmsApiSteps();
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));

        omsApiSteps.updateBuyerAccountId(lwaTestContext);
        omsApiSteps.updateBuyerAccountIp(lwaTestContext, "192.168.7.0");
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(lwaTestContext));

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext,10));

        omsApiSteps.updateBuyerAccountIp(lwaTestContext, "192.168.1.1");
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext,10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7803)
    @ParameterizedTest
    @CsvSource({"submitOrder-existedAllSysID.json"})
    public void accountValidationAllSysIdValid(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext,9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext,10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7820)
    @ParameterizedTest
    @CsvSource({"submitNewOrder.json"})
    public void accountValidationNoAllSysIdException(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext,9));
        errorMessage.append(new OrdersSteps().verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        errorMessage.append(new OrderStatusHistorySteps().verifyRowWithOrderId(lwaTestContext.getApiOrderId()));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    //ToDo: add last verification point
    @TestRailID(id = 7819)
    @ParameterizedTest
    @CsvSource({"submitOrder-matchedAllSysID.json"})
    public void accountValidationAllSysIdMatched(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        assertTrue(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo).isEmpty());
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext,9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext,10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }
}
