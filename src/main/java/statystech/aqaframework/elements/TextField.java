package statystech.aqaframework.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextField extends Element{

    private static final Logger logger = LoggerFactory.getLogger(TextField.class);

    WebElement element;
    By locator;

    public TextField(WebDriver webDriver, By locator){
        waitForElementToLoad(locator,webDriver);
        this.locator = locator;
        element = webDriver.findElement(locator);
    }

    public void click() {
        element.click();
        logger.info("TextField with locator clicked: " + locator);
    }

    public boolean isEnabled() {
        logger.info(("TextField '" + element.getText() + "' is enabled - " + element.isEnabled()));
        return element.isEnabled();
    }

    public void fillIn(String text){
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    public void fillInAndSubmit(String text){
        element.click();
        element.clear();
        element.sendKeys(text);
        element.sendKeys(Keys.ENTER);
    }
}
