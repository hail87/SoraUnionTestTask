package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.AddressTable;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.APIsteps.OmsApiSteps;
import statystech.aqaframework.steps.DBsteps.*;
import statystech.aqaframework.tests.ApiTestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.utils.DBCleaner;
import statystech.aqaframework.utils.DataUtils;
import statystech.aqaframework.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static statystech.aqaframework.steps.Steps.verifyExpectedResults;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class SubmitOrderTestSuite extends ApiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(SubmitOrderTestSuite.class);

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
        logger.info(String.format(
                "\nTest № %d has been started : '%s'\n", testInfo.getTestMethod().get().getAnnotation(TestRailID.class).id(), name));
        LwaTestContext lwaTestContext = new LwaTestContext(name);
        lwaTestContext.getConnection();
        Context.addTestContext(lwaTestContext);
    }

    @TestRailID(id = 7743)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-newBuyerC7743.json"})
    public void submitOrderNewBuyerAccountId(String jsonFilename, TestInfo testInfo) throws IOException {
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
        lwaTestContext.setSubmitOrderObjectsFromJson();
        errorMessage.append(new OrderItemSteps().checkProductIdsWithOrderID(lwaTestContext));
        errorMessage.append(new AccountAddressSteps().checkShippingAndBillingAddressesID(lwaTestContext));
        errorMessage.append(new BuyerAccountSteps().checkBuyerAccountId(lwaTestContext));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7774)
    @ParameterizedTest
    @CsvSource({"submitOrder-knownBuyerNewSAC7774-a.json, submitOrder-knownBuyerNewSAC7774-b.json"})
    public void submitOrderKnownBuyerNewShippingAddress(String jsonFilename1, String jsonFilename2, TestInfo testInfo) throws IOException {

        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        OmsApiSteps omsApiSteps = new OmsApiSteps();
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(jsonFilename1, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));

        ordersSteps.setOMSShippingAddressIDToContext(lwaTestContext);
        AddressSteps addressSteps = new AddressSteps();
        int omsShippingAddressID = lwaTestContext.getOmsShippingAddressID();
        errorMessage.append(addressSteps.checkAddressExist(omsShippingAddressID));
        AddressTable addressTable = new AddressTable();
        addressTable.setTableRowsQuantity();

        AccountAddressSteps accountAddressSteps = new AccountAddressSteps();
        errorMessage.append(accountAddressSteps.checkRowWithShippingAddressIdBuyerAccountIDAddressTypeCDAndSetAccountAddressID(lwaTestContext, "SA"));

        JsonUtils.loadObjectToContextAndGetString(jsonFilename2, testInfo.getTestMethod().get().getName());
        errorMessage.append(omsApiSteps.updateBuyerAccountIdAndSendPOST(lwaTestContext));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));
        int newOmsShippingAddressID = ordersSteps.getOMSShippingAddressID(lwaTestContext.getApiOrderId());
        errorMessage.append(addressTable.verifyTableRowsQuantityDidNotChange());

        errorMessage.append(verifyExpectedResults(omsShippingAddressID, newOmsShippingAddressID));
        errorMessage.append(addressSteps.checkAddressExist(lwaTestContext.getOmsShippingAddressID()));
        errorMessage.append(accountAddressSteps.checkRowWithShippingAddressIdBuyerAccountIDAddressTypeCDAndSetAccountAddressID(lwaTestContext, "SA"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7782)
    @ParameterizedTest
    @CsvSource({"submitOrder-newSA.json"})
    public void submitOrderAsNewBuyerAndCheckShippingAddress(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        OmsApiSteps omsApiSteps = new OmsApiSteps();
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));
        ordersSteps.setOMSShippingAddressIDToContext(lwaTestContext);
        AddressSteps addressSteps = new AddressSteps();
        int omsShippingAddressID = lwaTestContext.getOmsShippingAddressID();
        errorMessage.append(addressSteps.checkAddressExist(omsShippingAddressID));
        AddressTable addressTable = new AddressTable();
        addressTable.setTableRowsQuantity();
        AccountAddressSteps accountAddressSteps = new AccountAddressSteps();
        errorMessage.append(accountAddressSteps.checkRowWithShippingAddressIdBuyerAccountIDAddressTypeCDAndSetAccountAddressID(lwaTestContext, "SA"));

        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));
        int newOmsShippingAddressID = ordersSteps.getOMSShippingAddressID(lwaTestContext.getApiOrderId());
        errorMessage.append(addressTable.verifyTableRowsQuantityDidNotChange());
        errorMessage.append(verifyExpectedResults(omsShippingAddressID, newOmsShippingAddressID));
        errorMessage.append(addressSteps.checkAddressExist(lwaTestContext.getOmsShippingAddressID()));
        errorMessage.append(accountAddressSteps.checkRowWithShippingAddressIdBuyerAccountIDAddressTypeCDAndSetAccountAddressID(lwaTestContext, "SA"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7783)
    @ParameterizedTest
    @CsvSource({"submitOrder-newBuyerAndrew.json"})
    public void submitOrderExistedBuyerBillingAddress(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        OmsApiSteps omsApiSteps = new OmsApiSteps();
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));
        ordersSteps.setOMSBillingAddressIDToContext(lwaTestContext);

        AddressSteps addressSteps = new AddressSteps();
        int omsBillingAddressID = lwaTestContext.getOmsBillingAddressID();
        errorMessage.append(addressSteps.checkAddressExist(omsBillingAddressID));
        AddressTable addressTable = new AddressTable();
        addressTable.setTableRowsQuantity();

        AccountAddressSteps accountAddressSteps = new AccountAddressSteps();
        errorMessage.append(accountAddressSteps.checkRowWithBillingAddressIdBuyerAccountIDAddressTypeCDAndSetAccountAddressID(lwaTestContext, "BA"));

        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        if(!errorMessage.isEmpty()){
            assertTrue(errorMessage.isEmpty(), DBCleaner.cleanDBafter7783(errorMessage.toString()));
        }
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));

        int newOmsBillingAddressID = ordersSteps.getOMSBillingAddressID(lwaTestContext.getApiOrderId());
        errorMessage.append(addressTable.verifyTableRowsQuantityDidNotChange());
        errorMessage.append(verifyExpectedResults(omsBillingAddressID, newOmsBillingAddressID));
        errorMessage.append(addressSteps.checkAddressExist(omsBillingAddressID));

        errorMessage.append(accountAddressSteps.checkRowWithBillingAddressIdBuyerAccountIDAddressTypeCDAndSetAccountAddressID(lwaTestContext, "BA"));
        accountAddressSteps.accountAddressTable.setTableRowsQuantity();

        errorMessage.append(omsApiSteps.updateBuyerAccountIdAndSendPOST(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));

        newOmsBillingAddressID = ordersSteps.getOMSBillingAddressID(lwaTestContext.getApiOrderId());
        errorMessage.append(addressTable.verifyTableRowsQuantityDidNotChange());
        errorMessage.append(verifyExpectedResults(omsBillingAddressID, newOmsBillingAddressID));
        errorMessage.append(addressSteps.checkAddressExist(omsBillingAddressID));

        errorMessage.append(accountAddressSteps.accountAddressTable.verifyTableRowsQuantityDidNotChange());

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7791)
    @ParameterizedTest
    @CsvSource({"submitOrder-newLicenseC7791-a.json, submitOrder-newLicenseC7791-b.json "})
    public void submitOrderExistedBuyerLicense(String jsonFilename1, String jsonFilename2, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);

        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename1, testInfo));
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));
        ordersSteps.setOMSBuyerAccountLicenseIDToContext(lwaTestContext);
        errorMessage.append(new BuyerAccountLicenseSteps().checkBuyerAccountLicenseID(lwaTestContext));

        JsonUtils.loadObjectToContextAndGetString(jsonFilename2, testInfo.getTestMethod().get().getName());
        errorMessage.append(new OmsApiSteps().updateBuyerAccountIdAndSendPOST(lwaTestContext));

        ordersSteps.setOMSBuyerAccountLicenseIDToContext(lwaTestContext);
        errorMessage.append(new BuyerAccountLicenseSteps().checkBuyerAccountLicenseIDisOnlyOne(lwaTestContext));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7792)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-newBuyer.json"})
    public void submitOrderUnknownWebsite(String jsonFilename) {
        String errorMessage = new OmsApiSteps().sendPostRequestWithFakeApiKeyAndWaitForStatusCode(jsonFilename, 403);
        assertTrue(errorMessage.isEmpty(), errorMessage);
    }

    @TestRailID(id = 7793)
    @ParameterizedTest
    @CsvSource({"submitOrder-newPaymentMethod.json"})
    public void submitOrderNewAndExistedPaymentMethod(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();

        OmsApiSteps omsApiSteps = new OmsApiSteps();
        errorMessage.append(omsApiSteps.sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        OrdersSteps ordersSteps = new OrdersSteps();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));
        ordersSteps.setPaymentMethodIDtoContext();
        PaymentMethodSteps paymentMethodSteps = new PaymentMethodSteps();
        paymentMethodSteps.checkLineCreated(lwaTestContext);
        errorMessage.append(omsApiSteps.updateBuyerAccountIdAndSendPOST(testInfo));
        errorMessage.append(paymentMethodSteps.paymentMethodTable.verifyTableRowsQuantityDidNotChange());

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());

    }

    @TestRailID(id = 7801)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-newBuyer.json"})
    public void submitOrderValidMerchantAccountWrongApiKey(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(new OmsApiSteps().sendPostRequestWithWrongApiKeyAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
        errorMessage.append(new OrdersSteps().verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7817)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-unknownProduct.json"})
    public void submitOrderUnknownProduct(String jsonFilename) {
        String errorMessage = new OmsApiSteps().sendPostRequestAndWaitForStatusCode(jsonFilename, 400);
        assertTrue(errorMessage.isEmpty(), errorMessage);
    }

    @TestRailID(id = 7818)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-twoProducts.json"})
    public void submitOrderMultipleProducts(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        lwaTestContext.setSubmitOrderObjectsFromJson();
        OrdersSteps ordersSteps = new OrdersSteps();
        errorMessage.append(ordersSteps.checkApiResponse(lwaTestContext));
        errorMessage.append(new OrderItemSteps().checkProductIdsWithOrderID(lwaTestContext));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

    @TestRailID(id = 7921)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-withEmptyBasket.json"})
    public void submitOrderEmptyBasket(String jsonFilename) {
        String errorMessage = new OmsApiSteps().sendPostRequestAndWaitForStatusCode(jsonFilename, 400);
        assertTrue(errorMessage.isEmpty(), errorMessage);
    }

    @TestRailID(id = 7922)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-nonValidMid.json"})
    public void submitOrderNonValidMerchantAccount(String jsonFilename, TestInfo testInfo) throws IOException {
        StringBuilder errorMessage = new StringBuilder();
        LwaTestContext lwaTestContext = getLwaTestContext(testInfo);
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        errorMessage.append(new OrdersSteps().verifyOrderStatusName(lwaTestContext.getApiOrderId(), "Exception"));

        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }

}
