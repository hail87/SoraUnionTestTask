package statystech.aqaframework.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.elements.Button;

public class LoginPage extends PageObject {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private final WebDriver webDriver;
    By txtUsernameLocator = By.xpath("//input[@name=\"username\"]");
    By txtPasswordLocator = By.xpath("//input[@name=\"password\"]");
    By btnLogInLocator = By.xpath("//button[@type=\"submit\"]");
    By msgErrorMessage = By.xpath("//button[@type=\"submit\"]");

    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        super.webDriver = webDriver;
    }

    public LoginPage typeUsername(String username) {
        waitForElementToLoad(txtUsernameLocator);
        webDriver.findElement(txtUsernameLocator).sendKeys(username);
        logger.info("Username field with locator '" + btnLogInLocator + "' is filled in");
        return this;
    }

    public LoginPage typePassword(String password) {
        waitForElementToLoad(txtPasswordLocator);
        webDriver.findElement(txtPasswordLocator).sendKeys(password);
        logger.info("Password field with locator '" + btnLogInLocator + "' is filled in");
        return this;
    }

    public void clickLogIn() {
        new Button(webDriver, btnLogInLocator).click();
        logger.info("Login button with locator clicked");
    }
}
