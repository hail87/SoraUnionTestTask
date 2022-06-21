package statystech.aqaframework.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class PageObject {

    final int waitForElementDelay = 5;

    protected WebDriver webDriver;

    protected void waitForElementToLoad(By by, WebDriver webDriver){
        WebDriverWait wait = new WebDriverWait(webDriver,waitForElementDelay);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void waitForButtonToBeClickable(By by, WebDriver webDriver){
        WebDriverWait wait = new WebDriverWait(webDriver,waitForElementDelay);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    private void scrollToElement(WebDriver driver, WebElement webElement) throws Exception {
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoViewIfNeeded()", webElement);
        Thread.sleep(500);
    }

}
