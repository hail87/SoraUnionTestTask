package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropDown extends Element {

    private static final Logger logger = LoggerFactory.getLogger(DropDown.class);

    @Setter
    @Getter
    WebElement webElement;
    @Setter
    @Getter
    By locator;
    @Setter
    @Getter
    WebDriver webDriver;

    public DropDown(WebDriver webDriver, By locator) {
        super(webDriver,locator);
        setLocator(locator);
        setWebDriver(webDriver);
        waitForElementToLoad(locator, webDriver);
        setWebElement(webDriver.findElement(locator));
    }

    public void selectByVisibleText(String text) {
        WebElement webElement = this.webElement.findElement(By.xpath(".//li[text()=\"" + text + "\"]"));
        webElement.click();
        logger.info("Option selected at the DropDown : " + text);
    }
}
