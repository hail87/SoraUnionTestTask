package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextField extends Element{

    private static final Logger logger = LoggerFactory.getLogger(TextField.class);

    @Setter
    @Getter
    WebElement webElement;
    @Setter
    @Getter
    By locator;
    @Setter
    @Getter
    WebDriver webDriver;

    public TextField(WebDriver webDriver, By locator){
        super(webDriver,locator);
        setLocator(locator);
        setWebDriver(webDriver);
        waitForElementToLoad(locator, webDriver);
        setWebElement(webDriver.findElement(locator));
    }

    public void click() {
        webElement.click();
        logger.info("TextField with locator clicked: " + locator);
    }

    public boolean isDisabled() {
        logger.info(("TextField '" + webElement.getText() + "' is enabled - " + webElement.isEnabled()));
        return webElement.isEnabled();
    }

    public void fillIn(String text){
        webElement.click();
        webElement.clear();
        webElement.sendKeys(text);
    }

    public void fillInAndSubmit(String text){
        webElement.click();
        webElement.clear();
        webElement.sendKeys(text);
        webElement.sendKeys(Keys.ENTER);
    }
}
