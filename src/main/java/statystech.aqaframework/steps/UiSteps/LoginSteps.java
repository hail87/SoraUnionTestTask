package statystech.aqaframework.steps.UiSteps;

import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import statystech.aqaframework.PageObjects.LoginPage;
import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.steps.Steps;

public class LoginSteps extends Steps {

    WebDriver webDriver;

    public LoginSteps(TestInfo testInfo){
        webDriver = Context.getTestContext(testInfo.getTestMethod().get().getName(), UiTestContext.class).getWebDriver();
    }

    public MainPage login(String name, String pass){
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.typeUsername(name);
        loginPage.typePassword(pass);
        return loginPage.clickLogIn();
    }
}
