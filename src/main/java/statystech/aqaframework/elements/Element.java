package statystech.aqaframework.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Element {

    private static final Logger logger = LoggerFactory.getLogger(Element.class);

    final int waitForElementDelay = 5;

    protected void waitForElementToLoad(By by, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
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
