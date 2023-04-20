package statystech.aqaframework.tests.UI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.steps.UiSteps.LoginSteps;
import statystech.aqaframework.steps.UiSteps.MainSteps;
import statystech.aqaframework.tests.UiTestClass;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;

public class UI_SmokeTestSuite extends UiTestClass {

    private static final Logger logger = LoggerFactory.getLogger(UI_SmokeTestSuite.class);

    @BeforeEach
    public void setTestContext(TestInfo testInfo) throws SQLException, IOException {
        String name = testInfo.getTestMethod().get().getName();
        UiTestContext uiTestContext = new UiTestContext(name);
        uiTestContext.initializeWebDriver();
        Context.addTestContext(uiTestContext);

        LwaTestContext lwaTestContext = new LwaTestContext(name);
        lwaTestContext.getConnectionQA();
        Context.addTestContext(lwaTestContext);
        logger.info("Context set\n");
    }

    @Test
    public void login(TestInfo testInfo) {
        MainSteps mainSteps = new MainSteps(new LoginSteps(testInfo).login(
                DataUtils.getPropertyValue("users.properties", "whmName"),
                DataUtils.getPropertyValue("users.properties", "whmPass")), testInfo);
        String activeOrders = mainSteps.getMainPage().getActiveOrders();
    }


}
