package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.PageObject;

public class CheckBox extends Element {

    private static final Logger logger = LoggerFactory.getLogger(CheckBox.class);

    @Setter
    @Getter
    WebElement webElement;
    @Setter
    @Getter
    By locator;
    @Setter
    @Getter
    WebDriver webDriver;

    public CheckBox(WebDriver webDriver, By locator) {
        super(webDriver, locator);
        setLocator(locator);
        setWebDriver(webDriver);
        setWebElement(webDriver.findElement(locator));
    }

    public CheckBox(WebDriver webDriver, WebElement webElement) {
        super(webElement, webDriver);
        setWebDriver(webDriver);
        setWebElement(webElement);
    }

    public void check() {
        if (locator != null) {
            waitForElementToLoad(locator, webDriver);
            if (!isVisible(locator, webDriver)) {
                logger.error("Checkbox with locator IS NOT displayed: '" + locator + "'");
            }
            if (!isChecked())
                webElement.click();
            logger.info("Checkbox with locator checked: " + locator);
        } else {
            waitForElementToLoad(webElement, webDriver);
            if (!isVisible(webElement)) {
                logger.error("Checkbox IS NOT displayed: '" + webElement + "'");
            }
            if (!isChecked())
                webElement.click();
            logger.info("Checkbox checked: " + webElement);
        }
    }

    public void uncheck() {
        if (locator != null) {
            waitForElementToLoad(locator, webDriver);
            if (!isVisible(locator, webDriver)) {
                logger.error("Button with locator IS NOT displayed: '" + locator + "'");
            }
            if (isChecked())
                webElement.click();
            logger.info("Checkbox with locator unchecked: " + locator);
        } else {
            waitForElementToLoad(webElement, webDriver);
            if (!isVisible()) {
                logger.error("Button IS NOT displayed: '" + webElement);
            }
            if (isChecked())
                webElement.click();
            logger.info("Checkbox unchecked: " + webElement);
        }
    }

    public boolean isVisible() {
        if (locator != null) {
            return super.isVisible(locator, webDriver);
        } else {
            return super.isVisible(webElement);
        }
    }

    public boolean isChecked() {
        logger.info(("Checkbox checked - " + webElement.isSelected()));
        return webElement.findElement(By.xpath(".//../../..")).getText().contains("selected")
                || webElement.getAttribute("class").contains("checked");
    }
}
