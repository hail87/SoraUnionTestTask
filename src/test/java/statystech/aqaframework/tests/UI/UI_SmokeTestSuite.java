package statystech.aqaframework.tests.UI;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.steps.UiSteps.LoginSteps;
import statystech.aqaframework.tests.ApiTestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;
import statystech.aqaframework.tests.UiTestClass;
import statystech.aqaframework.utils.DataUtils;

import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        uiTestContext.initializeeWebDriver();
        Context.addTestContext(uiTestContext);
    }

    @TestRailID(id = 199752)
    @Test
    public void wareHouseSelection(TestInfo testInfo) {
        StringBuilder errorMessage = new StringBuilder();
        //UiTestContext uiTestContext = getUiTestContext(testInfo);
        LoginSteps loginSteps = new LoginSteps(testInfo);
        MainPage mainPage = loginSteps.login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass"));
        String activeOrders = mainPage.getActiveOrders();
        mainPage.chooseWarehouse("Iberianium");
        String activeWarehouseOrders = mainPage.getActiveOrders();
        mainPage.chooseWarehouse("All warehouses");
        String activeOrdersNew = mainPage.getActiveOrders();
        assertEquals(activeOrders, activeOrdersNew);
    }
}
