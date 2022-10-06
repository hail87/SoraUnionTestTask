package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.PageObject;

public class Element {

    private static final Logger logger = LoggerFactory.getLogger(Element.class);
    final int waitForElementDelay = 10;
    final int waitForJSDelay = 30;

    @Getter
    @Setter
    private WebElement webElement;
    @Getter
    @Setter
    private By locator;
    @Getter
    @Setter
    private WebDriver webDriver;

    public Element(WebElement webElement, WebDriver webDriver) {
        setWebElement(webElement);
        setWebDriver(webDriver);
        waitForElementToLoad(webElement, webDriver);
    }

    public Element(WebDriver webDriver, By locator) {
        setLocator(locator);
        setWebDriver(webDriver);
        waitForElementToLoad(locator, webDriver);
        setWebElement(webDriver.findElement(locator));
    }

    public String getXpathPath() {
        return PageObject.getXpathPath(webElement);
    }

    public By getXpath() {
        return PageObject.getXpath(webElement);
    }

    protected void waitForElementToLoad(By by, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void waitForElementToLoad(WebElement webElement, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public void waitForElementToLoad() {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void scrollToElement(By by, WebDriver webDriver) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoViewIfNeeded()", webDriver.findElement(by));
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    protected void scrollToElement(WebElement webElement, WebDriver webDriver) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoViewIfNeeded()", webElement);
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void waitForElementToBeClickable() {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForElementToBeClickable(WebDriver webDriver, By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public void waitForElementToBeClickable(WebDriver webDriver,  WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public void waitForElementToDisappear(By xpath, WebDriver webDriver) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, 2);
            wait.until(ExpectedConditions.invisibilityOf(webDriver.findElement(xpath)));
        } catch (NoSuchElementException e) {
            logger.info("waitForElementToDisappear: Element with locator not found as expected :" + xpath);
        }
    }

    public void waitForElementToDisappear(WebDriver webDriver) {
        if (webElement != null) {
            try {
                WebDriverWait wait = new WebDriverWait(webDriver, 3);
                wait.until(ExpectedConditions.invisibilityOf(webElement));
            } catch (NoSuchElementException e) {
                logger.info("waitForElementToDisappear: Element with locator not found as expected : " + webElement);
            }
        }
    }

    protected boolean isVisible(By locator, WebDriver webDriver) {
        WebElement element = webDriver.findElement(locator);
        logger.info(("Element with locator '" + locator + "' is visible - " + element.isDisplayed()));
        return element.isDisplayed();
    }

    protected boolean isVisible(WebElement webElement) {
        logger.info(("Element '" + webElement + "' is visible - " + webElement.isDisplayed()));
        return webElement.isDisplayed();
    }

    public boolean isVisible() {
        if (locator != null){
            return isVisible(locator, webDriver);
        } else {
            return isVisible(webElement);
        }
    }

    @Deprecated
    public boolean isEnabled(By locator, WebDriver webDriver) {
        WebElement element = webDriver.findElement(locator);
        logger.info(("Element with locator '" + locator + "' is enabled - " + element.isEnabled()));
        return element.isEnabled();
    }

    public boolean isEnabled(WebElement webElement) {
        logger.info(("Element '" + webElement + "' is enabled - " + webElement.isEnabled()));
        return webElement.isEnabled();
    }

    public boolean isRowDisabled() {
        WebElement webElement = webDriver.findElement(locator);
        logger.info(("Element with locator '" + locator + "' is enabled - " + webElement.isEnabled()));
        return webElement.getAttribute("class").contains("_disabled__");
    }

    public boolean isDisabled() {
        WebElement webElement = webDriver.findElement(locator);
        logger.info(("Element with locator '" + locator + "' is disabled - " + !webElement.isEnabled()));
        return !webElement.isEnabled();
    }

    public boolean isDisabled(WebDriver webDriver, By locator) {
        WebElement webElement = webDriver.findElement(locator);
        logger.info(("Element with locator '" + locator + "' is disabled - " + !webElement.isEnabled()));
        return !webElement.isEnabled();
    }

    public void hover() {
        Actions action = new Actions(webDriver);
        action.moveToElement(webElement).build().perform();
        logger.info(("Hover on element : '" + webElement + "'"));
    }

    public void hoverAndClick() {
        Actions action = new Actions(webDriver);
        action.moveToElement(webElement).moveToElement(webDriver.findElement(locator)).click().build().perform();
        logger.info(("Hover on element : '" + webElement + "', and then click at '" + locator + "'"));
    }

    public void hoverAndClickRelatedElement(By locator) {
        Actions action = new Actions(webDriver);
        action.moveToElement(webElement).moveToElement(webElement.findElement(locator)).click().build().perform();
        logger.info(("Hover on element : '" + webElement + "', and then click at '" + locator + "'"));
    }

    public void highLight() {
        if (webDriver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].style.border='3px solid red'", webElement);
        }
    }

    public boolean waitForJStoLoad(WebDriver webDriver) {

        WebDriverWait wait = new WebDriverWait(webDriver, waitForJSDelay);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) js.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return js.executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

}
