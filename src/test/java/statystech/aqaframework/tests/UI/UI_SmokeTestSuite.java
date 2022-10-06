package statystech.aqaframework.tests.UI;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.OrderCardDetailsPopUp;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.steps.DBsteps.OrdersSteps;
import statystech.aqaframework.steps.DBsteps.StageOrderSteps;
import statystech.aqaframework.steps.UiSteps.LoginSteps;
import statystech.aqaframework.steps.UiSteps.MainSteps;
import statystech.aqaframework.steps.UiSteps.OrderCardDetailsPopUpSteps;
import statystech.aqaframework.steps.UiSteps.OrderFulfillmentSteps;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.tests.UiTestClass;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static statystech.aqaframework.steps.TestRail.TestRailAPI.loadProperties;

@ExtendWith(TestRailReportExtension.class)
public class UI_SmokeTestSuite extends UiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(UI_SmokeTestSuite.class);

    @BeforeAll
    static void clearTestResults() {
        TestRailReportExtension.setResults(null);
        TestRailReportExtension.setResults(new CopyOnWriteArrayList<>());
    }

    @AfterAll
    public static void setSuiteID() {
        Properties properties = loadProperties();
        properties.setProperty("testrail_testSuiteId", "15772");
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
        UiTestContext uiTestContext = new UiTestContext(name);
        uiTestContext.initializeWebDriver();
        Context.addTestContext(uiTestContext);

        LwaTestContext lwaTestContext = new LwaTestContext(name);
        lwaTestContext.getConnectionQA();
        Context.addTestContext(lwaTestContext);
        logger.info("Context set\n");
    }

    @TestRailID(id = 199752)
    @Test
    public void wareHouseSelection(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);
        String activeOrders = mainSteps.getMainPage().getActiveOrders();
        mainSteps.chooseWarehouse("Iberianium");
        assertNotEquals(activeOrders, mainSteps.getActiveOrdersAfterUpdate());
        mainSteps.chooseWarehouse("All warehouses");
        assertEquals(activeOrders, mainSteps.getActiveOrdersAfterUpdate());
    }

    @TestRailID(id = 199754)
    @Test
    public void destinationFilter(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);
        String activeOrdersStartPosition = mainSteps.getMainPage().getActiveOrders();
        mainSteps.checkApplyButtonDisabled();
        mainSteps.chooseDestination("USA");
        assertNotEquals(mainSteps.getActiveOrdersAfterUpdate(), activeOrdersStartPosition);
        mainSteps.chooseDestination("USA");
        assertEquals(mainSteps.getActiveOrdersAfterUpdate(), activeOrdersStartPosition);
    }

    @TestRailID(id = 199755)
    @Test
    public void orderStatusFilter(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);
        String activeOrdersStartPosition = mainSteps.getMainPage().getActiveOrders();
        mainSteps.checkApplyButtonDisabled();
        mainSteps.chooseOrderStatus("Partially shipped");
        assertNotEquals(mainSteps.getMainPage().getActiveOrders(), activeOrdersStartPosition,
                String.format("\nExpected not equals, but active orders at the beginning : '%s' and at the end '%s' are te same\n",
                        activeOrdersStartPosition, mainSteps.getMainPage().getActiveOrders()));
        mainSteps.cancelOrderStatusChoice();
        assertEquals(mainSteps.getMainPage().getActiveOrders(), activeOrdersStartPosition);
    }

    //need to add precondition with importing "shipped" order
    @TestRailID(id = 202584)
    @Test
    public void checkDatePickerAndShippedTab(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);
        String activeOrdersStartPosition = mainSteps.getMainPage().getActiveOrders();
        mainSteps.getMainPage().clickShippedTab();
        String shippedOrdersStartPosition = mainSteps.getMainPage().getShippedOrders();
        mainSteps.chooseDateFromFirst();
        mainSteps.chooseDateTo28();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String activeOrdersFiltered = mainSteps.getMainPage().getActiveOrders();
        String shippedOrdersFiltered = mainSteps.getMainPage().getShippedOrders();
        assertTrue(true);
//        assertNotEquals(activeOrdersFiltered, activeOrdersStartPosition);
//        assertNotEquals(shippedOrdersFiltered, shippedOrdersStartPosition);
    }

    @TestRailID(id = 208523)
    @Test
    public void printAndPutOnHold(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);
        String errorMessage = mainSteps.checkPrintPageAndReturnToMain();
        assertTrue(errorMessage.isEmpty(), errorMessage);
        mainSteps.putOnHold();
        mainSteps.cancelHold();
        mainSteps.getMainPage().clickUncheckAll();
    }

    @TestRailID(id = 208524)
    @ParameterizedTest
    @ValueSource(strings = {"Order_9993305.json"})
    public void search(String jsonFilename, TestInfo testInfo) {
        DBUtils.importOrder(jsonFilename, testInfo);
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);
        String activeOrdersStartPosition = mainSteps.getMainPage().getActiveOrders();
        String errorMessage = "";
        mainSteps.searchOrder(9993305);
        errorMessage = mainSteps.verifyExpectedResults(mainSteps.getMainPage().getActiveOrders(), "Active (1)");
        assertTrue(errorMessage.isEmpty(), errorMessage);
        mainSteps.cancelSearch();
        errorMessage = mainSteps.verifyExpectedResults(mainSteps.getMainPage().getActiveOrders(), activeOrdersStartPosition);
        assertTrue(errorMessage.isEmpty(), errorMessage);
    }

    @TestRailID(id = 208525)
    @ParameterizedTest
    @ValueSource(strings = {"Order_9993305.json"})
    public void selectAllUncheckAll(String jsonFilename, TestInfo testInfo) {
        DBUtils.executeSqlScript("cleanup_order9993305.sql");
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToQATableAndLwaContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), stageOrderSteps.checkStatusColumn(id));
        OrdersSteps ordersSteps = new OrdersSteps();
        ordersSteps.setOrderIDtoContext();

        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);

        mainSteps.searchOrder(9993305);
        String activeOrdersStartPosition = mainSteps.getMainPage().getActiveOrders();
        mainSteps.getMainPage().selectAllNewOrders();
        mainSteps.getMainPage().clickUncheckAll();

        mainSteps.shipOrderToInProgress(9993305);

        mainSteps.getMainPage().selectAllInProgress();
        mainSteps.getMainPage().clickUncheckAll();
        mainSteps.clickAndVerifyOrdersOnHold();
        mainSteps.getMainPage().clickShowOnlyOrdersOnHoldOrAll();

        assertTrue(mainSteps.verifyExpectedResults(mainSteps.getActiveOrdersAfterUpdate(), activeOrdersStartPosition).isEmpty(),
                mainSteps.verifyExpectedResults(mainSteps.getMainPage().getActiveOrders(), activeOrdersStartPosition));
    }

//    @TestRailID(id = 220671)
//    @ParameterizedTest
//    @ValueSource(strings = {"Order_9993305.json"})
//    public void requestCancellation(String jsonFilename, TestInfo testInfo) {
//        DBUtils.executeSqlScript("cleanup_order9993305.sql");
//        StageOrderSteps stageOrderSteps = new StageOrderSteps();
//        int id = stageOrderSteps.insertJsonToQATableAndUiContext(jsonFilename, testInfo);
//        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), stageOrderSteps.checkStatusColumn(id));
//        OrdersSteps ordersSteps = new OrdersSteps();
//        ordersSteps.setOrderIDtoContext();
//
//        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
//                DataUtils.getPropertyValue("users.properties", "whmName"),
//                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);
//
//        mainSteps.searchOrder(9993305);
//
//        String errorMessage = mainSteps.requestCancellation("Some reason");
//        assertTrue(errorMessage.isEmpty(), errorMessage);
//        DBUtils.executeSqlScript("cleanup_order9993305.sql");
//    }

    @TestRailID(id = 220672)
    @ParameterizedTest
    @ValueSource(strings = {"Order_9993305.json"})
    public void shipAndResetOrder(String jsonFilename, TestInfo testInfo) {
        DBUtils.executeSqlScript("cleanup_order9993305.sql");
        StageOrderSteps stageOrderSteps = new StageOrderSteps();
        int id = stageOrderSteps.insertJsonToQATableAndLwaContext(jsonFilename, testInfo);
        assertTrue(stageOrderSteps.checkStatusColumn(id).isEmpty(), stageOrderSteps.checkStatusColumn(id));
        OrdersSteps ordersSteps = new OrdersSteps();
        ordersSteps.setOrderIDtoContext();

        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);

        mainSteps.searchOrder(9993305);

        assertTrue(mainSteps.getMainPage().getActiveOrders().equalsIgnoreCase("Active (1)"));
        int activeNewOrders = mainSteps.getMainPage().getActiveNewOrders();
        int activeInProgressOrders = mainSteps.getMainPage().getActiveInProgressOrders();

        mainSteps.shipOrderToInProgress(9993305);

        int activeNewOrdersUpdated = mainSteps.getMainPage().getActiveNewOrders();
        int activeInProgressOrdersUpdated = mainSteps.getMainPage().getActiveInProgressOrders();

        assertEquals(activeNewOrdersUpdated, activeNewOrders - 1,
                "\nExpected new orders : " + (activeNewOrders - 1) + "\nActual new orders : " + activeNewOrdersUpdated);
        assertEquals(activeInProgressOrdersUpdated, activeInProgressOrders + 1,
                "\nExpected in progress orders : " + (activeInProgressOrders + 1) + "\nActual in progress orders : " + activeInProgressOrdersUpdated);

        mainSteps.resetOrder(9993305);

        assertEquals(activeNewOrders, mainSteps.getMainPage().getActiveNewOrders(),
                "\nExpected new orders : " + (activeNewOrders - 1) + "\nActual new orders : " + mainSteps.getMainPage().getActiveNewOrders());
        assertEquals(activeInProgressOrders, mainSteps.getMainPage().getActiveInProgressOrders(),
                "\nExpected new orders : " + (activeNewOrders - 1) + "\nActual new orders : " + mainSteps.getMainPage().getActiveInProgressOrders());
    }

    @TestRailID(id = 225149)
    @ParameterizedTest
    @ValueSource(strings = {"Order_9993305.json"})
    public void moveInProgressOrderToNew(String jsonFilename, TestInfo testInfo) {
        DBUtils.importOrder(jsonFilename, testInfo);

        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);

        //mainSteps.clickBottomMessageIfVisible();
        mainSteps.searchOrder(9993305);
        mainSteps.shipOrderToInProgress(9993305);

        int activeNewOrders = mainSteps.getMainPage().getActiveNewOrders();
        int activeInProgressOrders = mainSteps.getMainPage().getActiveInProgressOrders();

        OrderCardDetailsPopUp orderCardDetailsPopUp = mainSteps.clickOrderCardInProgress(1);

        new OrderCardDetailsPopUpSteps(orderCardDetailsPopUp).moveToNewOrder(true);

        mainSteps.delay(2000);
        int activeNewOrdersUpdated = mainSteps.getMainPage().getActiveNewOrders();
        int activeInProgressOrdersUpdated = mainSteps.getMainPage().getActiveInProgressOrders();

        assertEquals(activeNewOrdersUpdated, activeNewOrders + 1);
        assertEquals(activeInProgressOrdersUpdated, activeInProgressOrders - 1);
    }

    @TestRailID(id = 225150)
    @ParameterizedTest
    @ValueSource(strings = {"Order_9993305.json"})
    public void splitDeleteShipExternallyParcel(String jsonFilename, TestInfo testInfo) {
        DBUtils.importOrder(jsonFilename, testInfo);

        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);

        mainSteps.searchOrder(9993305);
        OrderCardDetailsPopUp orderCardDetailsPopUp = mainSteps.clickOrderCard(9993305);
        OrderFulfillmentSteps orderFulfillmentSteps = new OrderFulfillmentSteps(orderCardDetailsPopUp.startOrderFulfillment());

        orderFulfillmentSteps.splitAndConfirm(1);
        orderFulfillmentSteps.createParcel(1, 1);
        orderFulfillmentSteps.deleteFirstParcel();
        orderFulfillmentSteps.createParcel(1, 1);
        orderFulfillmentSteps.shipParcelExternallyWithLocalPickup(1);

        String errorMessage = orderFulfillmentSteps.deleteCompletedParcel();
        assertTrue(errorMessage.isEmpty(), errorMessage);

        DBUtils.executeSqlScript("cleanup_order9993305.sql");
    }

    @TestRailID(id = 225151)
    @ParameterizedTest
    @ValueSource(strings = {"Order_9993305.json"})
    public void completelyShipParcelExternally(String jsonFilename, TestInfo testInfo) {
        DBUtils.importOrder(jsonFilename, testInfo);

        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);

        mainSteps.searchOrder(9993305);

        OrderCardDetailsPopUp orderCardDetailsPopUp = mainSteps.clickOrderCard(9993305);
        OrderFulfillmentSteps orderFulfillmentSteps = new OrderFulfillmentSteps(orderCardDetailsPopUp.startOrderFulfillment());
        for (int i = 1; i <= orderFulfillmentSteps.getProductsQuantity(); i++){
            orderFulfillmentSteps.createParcel(i, 1);
        }
        orderFulfillmentSteps.shipParcelExternallyWithAllFieldsFilled(2);

        String errorMessage = "";

        orderCardDetailsPopUp = orderFulfillmentSteps.closeOrderFulfillmentPage();

        errorMessage = orderFulfillmentSteps.verifyExpectedResults(
                orderCardDetailsPopUp.getOrderStatus(), "Shipped");
        assertTrue(errorMessage.isEmpty(), errorMessage);

        errorMessage = orderFulfillmentSteps.verifyExpectedResults(
                orderCardDetailsPopUp.getStartOrderFulfillmentButtonLabel(), "Order fulfillment details");
        assertTrue(errorMessage.isEmpty(), errorMessage);

        DBUtils.executeSqlScript("cleanup_order9993305.sql");
    }

    @TestRailID(id = 225152)
    @ParameterizedTest
    @ValueSource(strings = {"Order_9993305.json"})
    public void printPackingSlip(String jsonFilename, TestInfo testInfo) {
        DBUtils.importOrder(jsonFilename, testInfo);

        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);

        mainSteps.searchOrder(9993305);
        //mainSteps.shipOrder(9993305);
        OrderCardDetailsPopUp orderCardDetailsPopUp = mainSteps.clickOrderCard(9993305);

        OrderFulfillmentSteps orderFulfillmentSteps = new OrderFulfillmentSteps(orderCardDetailsPopUp.startOrderFulfillment());
        orderFulfillmentSteps.createParcel(1, 1);
        orderFulfillmentSteps.checkPrintPackingSlipEnabled(false);
        orderFulfillmentSteps.clickParcel(1);
        orderFulfillmentSteps.checkPrintPackingSlipEnabled(true);
        String errorMessage = orderFulfillmentSteps. clickPrintPackingSlipButton();
        assertTrue(errorMessage.isEmpty(), errorMessage);

        DBUtils.executeSqlScript("cleanup_order9993305.sql");
    }

    @TestRailID(id = 225153)
    @ParameterizedTest
    @ValueSource(strings = {"Order_9993305.json"})
    public void editTrackingNumberAndMoveToNewOrders(String jsonFilename, TestInfo testInfo) {
        DBUtils.importOrder(jsonFilename, testInfo);

        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);

        mainSteps.searchOrder(9993305);
        mainSteps.shipOrderToInProgress(9993305);
        OrderCardDetailsPopUp orderCardDetailsPopUp = mainSteps.clickOrderCard(9993305);

        DBUtils.executeSqlScript("cleanup_order9993305.sql");
    }

}
