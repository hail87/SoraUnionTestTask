package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void check() {
        waitForElementToLoad(locator, webDriver);
        if (!isVisible(locator, webDriver)) {
            logger.error("Checkbox with locator IS NOT displayed: '" + locator + "'");
        }
        if (!isChecked())
            webElement.click();
        logger.info("Checkbox with locator checked: " + locator);
    }

    public void uncheck() {
        waitForElementToLoad(locator, webDriver);
        if (!isVisible(locator, webDriver)) {
            logger.error("Button with locator IS NOT displayed: '" + locator + "'");
        }
        if (isChecked())
            webElement.click();
        logger.info("Checkbox with locator unchecked: " + locator);
    }

    public boolean isVisible() {
        return super.isVisible(locator, webDriver);
    }

    public boolean isChecked() {
        logger.info(("Checkbox with locator '" + locator + "' is checked - " + webElement.isSelected()));
        return webElement.findElement(By.xpath(".//../../..")).getText().contains("selected");
    }
}
