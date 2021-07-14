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
import statystech.aqaframework.TableObjects.AddressTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.OmsApiSteps;
import statystech.aqaframework.steps.DBsteps.*;
import statystech.aqaframework.tests.TestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.utils.DBUtils;
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
        logger.info(String.format("\nTest started : %s\n", name));
        LwaTestContext lwaTestContext = new LwaTestContext(name);
        lwaTestContext.getConnection();
        Context.addTestContext(lwaTestContext);
    }

    @TestRailID(id = 7809)
    @ParameterizedTest
    @CsvSource({"submitNewOrder.json"})
    public void verifySuspiciousAccount(String jsonFilename, TestInfo testInfo) throws IOException {
        OmsApiSteps omsApiSteps = new OmsApiSteps();
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        omsApiSteps.updateBuyerAccountId(lwaTestContext);
        omsApiSteps.updateBuyerAccountIp(lwaTestContext, "192.168.7.0");
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(lwaTestContext));

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 10));

        omsApiSteps.updateBuyerAccountIp(lwaTestContext, "192.168.1.1");
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7803)
    @ParameterizedTest
    @CsvSource({"submitOrder-existedAllSysID.json"})
    public void accountValidationAllSysIdValid(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7820)
    @ParameterizedTest
    @CsvSource({"submitNewOrder.json"})
    public void accountValidationNoAllSysIdException(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 9));
        errorMessage.append(new OrdersSteps().verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        errorMessage.append(new OrderStatusHistorySteps().checkRowWithOrderIdIsPresent(lwaTestContext.getApiOrderId()));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7819)
    @ParameterizedTest
    @CsvSource({"submitOrder-matchedAllSysID.json"})
    public void accountValidationAllSysIdMatched(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));
        errorMessage.append(new BuyerAccountSteps().checkAllSysAccountIDMatch(9998, 9997));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7804)
    @ParameterizedTest
    @CsvSource({"OrderValidation-NoValidAddress.json"})
    public void noValidAddressException(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        AddressTable addressTable = new AddressTable();
        addressTable.setTableRowsQuantity();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(addressTable.verifyTableRowsQuantityDidNotChange());

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.checkOMSShippingAddressID(lwaTestContext.getApiOrderId(), 636));
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 8));

        errorMessage.append(new OrderStatusHistorySteps().validateRowWithOrderIdHasOrderStatusID(lwaTestContext.getApiOrderId(), 19));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7812)
    @ParameterizedTest
    @CsvSource({"OrderValidation-ExternalAddressValidationFailed.json"})
    public void externalAddressValidationFailed(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        AddressTable addressTable = new AddressTable();
        addressTable.setTableRowsQuantity();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(addressTable.verifyNewRowCreated());
        errorMessage.append(new AddressSteps().verifyVerificationStatus("error"));

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrdersSteps ordersSteps = new OrdersSteps();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 8));

        errorMessage.append(new OrderStatusHistorySteps().validateRowWithOrderIdHasOrderStatusID(lwaTestContext.getApiOrderId(), 19));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7821)
    @ParameterizedTest
    @CsvSource({"OrderValidation - addressValid.json"})
    public void addressValid(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        AddressTable addressTable = new AddressTable();
        addressTable.setTableRowsQuantity();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(addressTable.verifyTableRowsQuantityDidNotChange());

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.checkOMSShippingAddressID(lwaTestContext.getApiOrderId(), 296));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 8));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7822)
    @ParameterizedTest
    @CsvSource({"OrderValidation - newAddressValid.json"})
    public void addressValidatedByExternalService(String jsonFilename, TestInfo testInfo) throws IOException {

        StringBuilder errorMessage = new StringBuilder();
        AddressTable addressTable = new AddressTable();
        addressTable.setTableRowsQuantity();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(addressTable.verifyNewRowCreated());
        AddressSteps addressSteps = new AddressSteps();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        errorMessage.append(addressSteps.verifyVerificationStatus("verified"));
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);


        errorMessage.append(new OrdersSteps().verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));

        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 8));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));

        try {
            DBUtils.cleanDB("clean_all_lwa_test_data.sql");
            DBUtils.cleanDB("clean_new_address.sql");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

    }
}
