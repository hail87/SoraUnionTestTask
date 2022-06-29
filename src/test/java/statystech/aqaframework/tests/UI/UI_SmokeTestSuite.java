package statystech.aqaframework.tests.UI;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.steps.UiSteps.LoginSteps;
import statystech.aqaframework.steps.UiSteps.MainSteps;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.tests.UiTestClass;
import statystech.aqaframework.utils.DataUtils;

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
    public void setTestContext(TestInfo testInfo) {
        String name = testInfo.getTestMethod().get().getName();
        logger.info(String.format(
                "\nTest № %d has been started : '%s'\n", testInfo.getTestMethod().get().getAnnotation(TestRailID.class).id(), name));
        UiTestContext uiTestContext = new UiTestContext(name);
        uiTestContext.initializeWebDriver();
        Context.addTestContext(uiTestContext);
    }

    @TestRailID(id = 199752)
    @Test
    public void wareHouseSelection(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")));
        String activeOrders = mainSteps.getMainPage().getActiveOrders();
        mainSteps.chooseWarehouse("Iberianium");
        mainSteps.getMainPage().getActiveOrders();
        mainSteps.chooseWarehouse("All warehouses");
        String activeOrdersNew = mainSteps.getMainPage().getActiveOrders();
        assertEquals(activeOrders, activeOrdersNew);
    }

    @TestRailID(id = 199754)
    @Test
    public void destinationFilter(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")));
        String activeOrdersStartPosition = mainSteps.getMainPage().getActiveOrders();
        mainSteps.checkApplyButtonDisabled();
        mainSteps.chooseDestination("USA");
        mainSteps.clickApplyButton();
        assertNotEquals(mainSteps.getMainPage().getActiveOrders(), activeOrdersStartPosition);
        mainSteps.chooseDestination("USA");
        mainSteps.clickApplyButton();
        assertEquals(mainSteps.getMainPage().getActiveOrders(), activeOrdersStartPosition);
    }

    @TestRailID(id = 199755)
    @Test
    public void orderStatusFilter(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")));
        String activeOrdersStartPosition = mainSteps.getMainPage().getActiveOrders();
        mainSteps.checkApplyButtonDisabled();
        mainSteps.chooseOrderStatus("Partially shipped");
        mainSteps.clickApplyButton();
        assertNotEquals(mainSteps.getMainPage().getActiveOrders(), activeOrdersStartPosition);
        mainSteps.cancelOrderStatusChoice();
        assertEquals(mainSteps.getMainPage().getActiveOrders(), activeOrdersStartPosition);
    }

//    @TestRailID(id = 202584)
//    @Test
//    public void checkDatePickerAndShippedTab(TestInfo testInfo) {
//        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
//                DataUtils.getPropertyValue("users.properties", "whmName"),
//                DataUtils.getPropertyValue("users.properties", "whmPass")));
//        String activeOrdersStartPosition = mainSteps.getMainPage().getActiveOrders();
//        mainSteps.getMainPage().clickShippedTab();
//        String shippedOrdersStartPosition = mainSteps.getMainPage().getShippedOrders();
//        //mainSteps.setDateFrom("06/01/2022");
//        //mainSteps.setDateTo("06/24/2022");
//        mainSteps.getMainPage().selectDayPriorToToday(7);
//        String activeOrdersFiltered = mainSteps.getMainPage().getActiveOrders();
//        String shippedOrdersFiltered= mainSteps.getMainPage().getShippedOrders();
//        assertNotEquals(activeOrdersFiltered, activeOrdersStartPosition);
//        assertNotEquals(shippedOrdersFiltered, shippedOrdersStartPosition);
//        assertEquals(activeOrdersFiltered, "Active (60)");
//        assertEquals(activeOrdersFiltered, "Shipped (3)");
//    }
}
