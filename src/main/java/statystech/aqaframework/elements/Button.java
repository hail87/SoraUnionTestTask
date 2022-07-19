package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Button extends Element {

    private static final Logger logger = LoggerFactory.getLogger(Button.class);

    @Setter
    @Getter
    WebElement element;
    By locator;
    WebDriver webDriver;

    public Button(WebDriver webDriver, By locator) {
        this.locator = locator;
        this.webDriver = webDriver;
        setElement(webDriver.findElement(locator));
    }

    public void click() {
        waitForElementToLoad(locator, webDriver);
        if (!isVisible(locator, webDriver)) {
            logger.error("Button with locator IS NOT displayed: '" + locator + "'");
        }
        if (!isEnabled(locator, webDriver)) {
            logger.error("Button with locator IS NOT clickable: '" + locator + "'");
        }
        element.click();
        logger.info("Button with locator clicked: " + locator);
    }

    public String getText() {
        String buttonText = element.getText();
        logger.info(("Button with locator '" + locator + "' text is " + buttonText));
        return buttonText;
    }

    protected boolean isVisible() {
        return super.isVisible(locator, webDriver);
    }

    public boolean isEnabled() {
        return super.isEnabled(locator, webDriver);
    }
}
