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
    WebElement element;
    By locator;
    WebDriver webDriver;

    public CheckBox(WebDriver webDriver, By locator) {
        this.locator = locator;
        this.webDriver = webDriver;
        setElement(webDriver.findElement(locator));
    }

    public void check() {
        waitForElementToLoad(locator, webDriver);
        if (!isVisible(locator, webDriver)) {
            logger.error("Button with locator IS NOT displayed: '" + locator + "'");
        }
        element.click();
        logger.info("Button with locator clicked: " + locator);
    }

//    public String getText() {
//    }

    protected boolean isVisible() {
        return super.isVisible(locator, webDriver);
    }

    public boolean isChecked() {
        logger.info(("Checkbox with locator '" + locator + "' is checked - " + element.isSelected()));
        return element.isSelected();
    }
}
