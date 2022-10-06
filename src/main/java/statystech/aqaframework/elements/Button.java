package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Button extends Element {

    private static final Logger logger = LoggerFactory.getLogger(Button.class);

    @Setter
    @Getter
    WebElement webElement;
    @Setter
    @Getter
    By locator;
    @Setter
    @Getter
    WebDriver webDriver;

    public Button(WebDriver webDriver, By locator) {
        super(webDriver, locator);
        setLocator(locator);
        setWebDriver(webDriver);
        waitForElementToLoad(locator, webDriver);
        setWebElement(webDriver.findElement(locator));
    }

    public Button(WebDriver webDriver, WebElement webElement) {
        super(webElement, webDriver);
        setWebDriver(webDriver);
        waitForElementToLoad(webElement, webDriver);
        setWebElement(webElement);
    }

    public void click() {
        if (locator != null) {
            waitForElementToLoad(locator, webDriver);
            if (!isVisible(locator, webDriver)) {
                logger.error("Button with locator IS NOT displayed: '" + locator + "'");
            }
            if (!isEnabled(locator, webDriver)) {
                logger.error("Button with locator IS NOT clickable: '" + locator + "'");
            }
            waitForElementToBeClickable(webDriver, locator);
            webElement.click();
            logger.info("Button with locator clicked: " + locator);
        } else {
            waitForElementToLoad(webElement, webDriver);
            if (!isVisible(webElement)) {
                logger.error("Button with locator IS NOT displayed: '" + webElement + "'");
            }
            if (!isEnabled(webElement)) {
                logger.error("Button with locator IS NOT clickable: '" + webElement + "'");
            }
            waitForElementToBeClickable(webDriver, webElement);
            webElement.click();
            logger.info("Button with locator clicked: " + webElement);
        }
    }

    public String getText() {
        String buttonText = webElement.getText();
        logger.info(("Button with locator '" + locator + "' text is " + buttonText));
        return buttonText;
    }

    public void waitForElementToBeClickable() {
        WebDriverWait wait = new WebDriverWait(this.webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.elementToBeClickable(this.locator));
    }

    public boolean isEnabled() {
        return webElement.isEnabled();
    }

    public boolean isDisabled() {
        return super.isDisabled(webDriver, locator);
    }
}
