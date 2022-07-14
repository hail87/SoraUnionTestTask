package statystech.aqaframework.steps.UiSteps;

import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.LoginPage;
import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.steps.Steps;

public class LoginSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);

    WebDriver webDriver;

    public LoginSteps(TestInfo testInfo){
        webDriver = Context.getTestContext(testInfo.getTestMethod().get().getName(), UiTestContext.class).getWebDriver();
    }

    public MainPage login(String name, String pass){
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.typeUsername(name);
        loginPage.typePassword(pass);
        logger.info("Log in");
        return loginPage.clickLogIn();
    }
}
