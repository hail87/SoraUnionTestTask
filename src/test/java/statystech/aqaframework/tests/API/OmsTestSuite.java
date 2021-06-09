package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.OmsApiSteps;
import statystech.aqaframework.steps.DBsteps.*;
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
public class OmsTestSuite extends TestClass {

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
        LwaTestContext lwaTestContext = new LwaTestContext(testInfo.getTestMethod().get().getName());
        lwaTestContext.getConnection();
        Context.addTestContext(lwaTestContext);
    }

    @TestRailID(id = 7743)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-newBuyer.json"})
    public void submitOrderNoBuyerAccountId(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));

        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));
        ordersSteps.setOMSShippingAddressIDToContext(lwaTestContext);
        ordersSteps.setOMSBillingAddressIDToContext(lwaTestContext);
        AddressSteps addressSteps = new AddressSteps();
        errorMessage.append(addressSteps.checkAddressExist(lwaTestContext.getOmsShippingAddressID()));
        errorMessage.append(addressSteps.checkAddressExist(lwaTestContext.getOmsBillingAddressID()));
        ordersSteps.setOMSBuyerAccountLicenseIDToContext(lwaTestContext);
        errorMessage.append(new BuyerAccountLicenseSteps().checkBuyerAccountLicenseID(lwaTestContext));
        errorMessage.append(new OrderItemSteps().checkOrderID(lwaTestContext));
        errorMessage.append(new AccountAddressSteps().checkShippingAndBillingAddressesID(lwaTestContext));
        errorMessage.append(new BuyerAccountSteps().checkBuyerAccountId(lwaTestContext));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }
}
