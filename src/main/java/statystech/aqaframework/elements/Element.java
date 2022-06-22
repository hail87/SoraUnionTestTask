package statystech.aqaframework.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class Element {

    final int waitForElementDelay = 5;

    protected void waitForElementToLoad(By by, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

}
