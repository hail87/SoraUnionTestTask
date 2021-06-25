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
import statystech.aqaframework.steps.DBsteps.OrdersSteps;
import statystech.aqaframework.steps.DBsteps.PaymentMethodSteps;
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

    //@TestRailID(id = 7809)
    @ParameterizedTest
    @CsvSource({"submitOrder-newPaymentMethod.json"})
    public void verifySuspiciousAccount(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();

        OmsApiSteps omsApiSteps = new OmsApiSteps();
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));
        ordersSteps.setPaymentMethodID();
        PaymentMethodSteps paymentMethodSteps = new PaymentMethodSteps();
        paymentMethodSteps.checkLineCreated(lwaTestContext);
        errorMessage.append(omsApiSteps.updateBuyerAccountIdAndSendPOST(testInfo));
        paymentMethodSteps.paymentMethodTable.verifyTableRowsQuantityDidNotChange();
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }
}
