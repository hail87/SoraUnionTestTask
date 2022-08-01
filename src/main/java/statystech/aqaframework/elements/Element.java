package statystech.aqaframework.elements;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Element {

    private static final Logger logger = LoggerFactory.getLogger(Element.class);

    final int waitForElementDelay = 3;

    protected void waitForElementToLoad(By by, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void waitForElementToDisappear(By xpath, WebDriver webDriver) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, 2);
            wait.until(ExpectedConditions.invisibilityOf(webDriver.findElement(xpath)));
        } catch (NoSuchElementException e) {
            logger.info("waitForElementToDisappear: Element with locator not found as expected :" + xpath);
        }
    }

    protected boolean isVisible(By locator, WebDriver webDriver) {
        WebElement element = webDriver.findElement(locator);
        logger.info(("Element with locator '" + locator + "' is visible - " + element.isDisplayed()));
        return element.isDisplayed();
    }

    public boolean isEnabled(By locator, WebDriver webDriver) {
        WebElement element = webDriver.findElement(locator);
        logger.info(("Element with locator '" + locator + "' is enabled - " + element.isEnabled()));
        return element.isEnabled();
    }

}
