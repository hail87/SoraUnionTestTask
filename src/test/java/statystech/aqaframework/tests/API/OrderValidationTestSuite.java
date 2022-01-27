package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.*;
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
import statystech.aqaframework.utils.JsonUtils;

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
        properties.setProperty("testrail_testSuiteId", "5960");
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

//    @TestRailID(id = 1111)
//    @Test
//    public void encryptDB() throws SQLException {
//        String s = "";
//    }


    //https://statystech.atlassian.net/browse/OMS-854
    @TestRailID(id = 7803)
    @ParameterizedTest
    @CsvSource({"submitOrder-existedAllSysID.json"})
    public void accountValidationAllSysIdValid(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7804)
    @ParameterizedTest
    @CsvSource({"OrderValidation-NoValidAddress.json"})
    public void noValidAddressException(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        AddressTable addressTable = new AddressTable();
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        addressTable.setTableRowsQuantity();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(addressTable.verifyTableRowsQuantityDidNotChange());
        errorMessage.append(ordersSteps.checkOMSShippingAddressID(lwaTestContext.getApiOrderId(), 7236)); //638 2780 6866
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 8));

        errorMessage.append(new OrderStatusHistorySteps().validateRowWithOrderIdHasOrderStatusID(lwaTestContext.getApiOrderId(), 19));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7805)
    @ParameterizedTest
    @CsvSource({"OrderValidation - VerifyLicenseExpiredException.json"})
    public void verifyLicenseExpiredException(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(new OrderExceptionHistorySteps().verifyOrderExceptionTypeID(lwaTestContext, 4));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7806)
    @ParameterizedTest
    @CsvSource({"OrderValidation - VerifyLicenseToValidateException.json"})
    public void verifyLicenseToValidateException(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(new OrderExceptionHistorySteps().verifyOrderExceptionTypeID(lwaTestContext, 5));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7807)
    @ParameterizedTest
    @CsvSource({"OrderValidation - VerifyLicenseNeededAndDontExist.json"})
    public void verifyNoLicenseException(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 6));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7808)
    @ParameterizedTest
    @CsvSource({"OrderValidation - VerifyInsufficientInventoryExceptionEnoughInventory.json"})
    public void verifyInsufficientInventoryExceptionEnoughInventory(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Confirmed"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
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
        omsApiSteps.updateBuyerAccountId(lwaTestContext, "192.168.7.0");
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(lwaTestContext));

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 10));

        omsApiSteps.updateBuyerAccountId(lwaTestContext, "192.168.1.1");
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7810)
    @ParameterizedTest
    @CsvSource({"OrderValidation - VerifyPaymentSuccessful.json"})
    public void verifyPaymentSuccessful(String jsonFileName, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        OmsApiSteps omsApiSteps = new OmsApiSteps();
        String orderID = DataUtils.getAlphaNumericRandom(2, 3);
        logger.info("\nGenerated orderID: '" + orderID + "'");
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        omsApiSteps.aretoAuthorizeAndSaveResponseToContext(orderID, lwaTestContext);
        //omsApiSteps.aretoCaptureAndSaveResponseToContext(lwaTestContext.getAretoAuthorizeAmount(), lwaTestContext.getAretoInternalOrderId(), lwaTestContext);

        String updatedJson = JsonUtils.getStringFromJson(jsonFileName).replace("\"website_order_id\": \"\"", "\"website_order_id\": \"" + orderID + "\"");
        updatedJson = updatedJson.replace("\"payment_transaction_id\": \"\"", "\"payment_transaction_id\": \"" + lwaTestContext.getAretoInternalOrderId() + "\"");
        updatedJson = updatedJson.replace("\"payment_token\": \"\"", "\"payment_token\": \"" + lwaTestContext.getAretoToken() + "\"");
        //logger.info("\nUpdated .json:\n" + updatedJson);
        lwaTestContext.setJsonString(updatedJson);
        Context.updateTestContext(lwaTestContext);
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Confirmed"));
        errorMessage.append(ordersSteps.verifyOMSisPaid("1", lwaTestContext.getApiOrderId()));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7811)
    @ParameterizedTest
    @CsvSource({"OrderValidation - VerifyPaymentFailedException.json"})
    public void verifyPaymentFailedException(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 3));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7812)
    @ParameterizedTest
    @CsvSource({"OrderValidation-ExternalAddressValidationFailed.json"})
    public void externalAddressValidationFailed(String jsonFilename, TestInfo testInfo) throws IOException {
        try {
            DBUtils.cleanDB("clean_new_address_failed.sql");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        StringBuilder errorMessage = new StringBuilder();
        AddressTable addressTable = new AddressTable();
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        addressTable.setTableRowsQuantity();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(addressTable.verifyNewRowCreated());
        errorMessage.append(new AddressSteps().verifyVerificationStatus("IECCrPzcm85scw6YQCrSzw==", "error")); //Smithello = IECCrPzcm85scw6YQCrSzw==
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 8));

        errorMessage.append(new OrderStatusHistorySteps().validateRowWithOrderIdHasOrderStatusID(lwaTestContext.getApiOrderId(), 19));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7815)
    @ParameterizedTest
    @CsvSource({"OrderValidation - VerifyLicenseNeededAndExist.json"})
    public void verifyLicenseNeededAndExist(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        if (!ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Confirmed").isEmpty()) {
            errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
            assertTrue(errorMessage.isEmpty(), errorMessage.toString());
            OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
            errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
            assertTrue(errorMessage.isEmpty(), errorMessage.toString());
            errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 1));
            errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 4));
            errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 5));
            errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 6));
            errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 8));
            errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
            errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));
            assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        } else {
            errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Confirmed"));
            assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        }
    }

    @TestRailID(id = 7819)
    @ParameterizedTest
    @CsvSource({"submitOrder-matchedAllSysID.json"})
    public void accountValidationAllSysIdMatched(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        OrdersSteps ordersSteps = new OrdersSteps();
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));
        errorMessage.append(new BuyerAccountSteps().checkAllSysAccountIDMatch(9998, 9997));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7820)
    @ParameterizedTest
    @CsvSource({"submitNewOrder.json"})
    public void accountValidationNoAllSysIdException(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        OrdersSteps ordersSteps = new OrdersSteps();
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 9));
        errorMessage.append(new OrderStatusHistorySteps().checkRowWithOrderIdIsPresent(lwaTestContext.getApiOrderId()));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7821)
    @ParameterizedTest
    @CsvSource({"OrderValidation - addressValid.json"})
    public void addressValid(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        AddressTable addressTable = new AddressTable();
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        addressTable.setTableRowsQuantity();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(addressTable.verifyTableRowsQuantityDidNotChange());

        errorMessage.append(ordersSteps.checkOMSShippingAddressID(lwaTestContext.getApiOrderId(), 7192));
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

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

        try {
            //DBUtils.cleanDB("clean_all_lwa_test_data.sql");
            DBUtils.cleanDB("clean_new_address.sql");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        StringBuilder errorMessage = new StringBuilder();
        AddressTable addressTable = new AddressTable();
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        addressTable.setTableRowsQuantity();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        ordersSteps.setOMSIsPaid(1, lwaTestContext.getApiOrderId());
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        errorMessage.append(addressTable.verifyNewRowCreated());
        AddressSteps addressSteps = new AddressSteps();

        errorMessage.append(addressSteps.verifyVerificationStatus("FBpmxSt++r8j0+MZOjk+8w==", "verified"));

        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));

        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 8));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }


    @TestRailID(id = 13175)
    @ParameterizedTest
    @CsvSource({"OrderValidation - VerifyLicenseNotNeededAndDontExist.json"})
    public void verifyLicenseNotNeededAndDontExist(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrdersSteps ordersSteps = new OrdersSteps();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 1));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 4));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 5));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 6));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 8));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 9));
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeIDIsNot(lwaTestContext, 10));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 14449)
    @ParameterizedTest
    @CsvSource({"OrderValidation - VerifyInsufficientInventoryExceptionNotEnoughInventory.json"})
    public void verifyInsufficientInventoryExceptionNotEnoughInventory(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

        OrderExceptionHistorySteps orderExceptionHistorySteps = new OrderExceptionHistorySteps();
        errorMessage.append(orderExceptionHistorySteps.verifyRowWithOrderIdExist(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(orderExceptionHistorySteps.verifyOrderExceptionTypeID(lwaTestContext, 7));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }
}
