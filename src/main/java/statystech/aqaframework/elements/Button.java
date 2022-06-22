package statystech.aqaframework.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Button extends Element{

    private static final Logger logger = LoggerFactory.getLogger(Button.class);

    WebElement element;
    By locator;

    public Button(WebDriver webDriver, By locator){
        waitForElementToLoad(locator,webDriver);
        this.locator = locator;
        element = webDriver.findElement(locator);
    }

    public void click() {
        element.click();
        logger.info("Button with locator clicked: " + locator);
    }

    public boolean isEnabled() {
        logger.info(("Button '" + element.getText() + "' is enabled - " + element.isEnabled()));
        return element.isEnabled();
    }
}
