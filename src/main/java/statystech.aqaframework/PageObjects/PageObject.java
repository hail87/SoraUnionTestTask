package statystech.aqaframework.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PageObject {

    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);

    final int waitForElementDelay = 5;

    protected WebDriver webDriver;

    protected void waitForElementToLoad(By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void waitForElementToDisappear(WebElement element) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    protected void waitForButtonToBeClickable(By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    protected void scrollToElement(WebElement webElement) throws Exception {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoViewIfNeeded()", webElement);
        Thread.sleep(500);
    }

    protected WebElement findElementByText(String text) {
        logger.info("Looking for element with text : " + text);
        return webDriver.findElement(By.xpath("//div[text()=\"" + text + "\"]"));
    }

    protected void unfocus(){
        webDriver.findElement(By.xpath("//body")).click();
    }

}
