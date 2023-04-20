package statystech.aqaframework.tests.UI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.steps.UiSteps.LoginSteps;
import statystech.aqaframework.steps.UiSteps.MainSteps;
import statystech.aqaframework.tests.UiTestClass;
import statystech.aqaframework.utils.DataUtils;

public class UI_SmokeTestSuite extends UiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(UI_SmokeTestSuite.class);

    @BeforeEach
    public void setTestContext(TestInfo testInfo) {
        String name = testInfo.getTestMethod().get().getName();
        UiTestContext uiTestContext = new UiTestContext(name);
        uiTestContext.initializeWebDriver();
        Context.addTestContext(uiTestContext);
        logger.info("Context set\n");
    }

    @Test
    public void login(TestInfo testInfo) {
        LoginSteps loginSteps = new LoginSteps(testInfo);
        MainPage mainPage = loginSteps.login(
                DataUtils.getPropertyValue("users.properties", "name"),
                DataUtils.getPropertyValue("users.properties", "pass"));
        loginSteps.verifyExpectedResults(mainPage.getUrl(), DataUtils.getPropertyValue("url.properties", "mainPage"));
    }

    @Test
    public void loginWrongCreds(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);

    }

    @Test
    public void loginEmptyCreds(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);

    }
}
