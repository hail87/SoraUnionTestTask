package statystech.aqaframework.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.LoginPage;

public class Button extends Element{

    private static final Logger logger = LoggerFactory.getLogger(Button.class);

    WebElement element;

    public Button(WebDriver webDriver, By locator){
        element = webDriver.findElement(locator);
    }

    public void click() {
        element.click();
        logger.info("Button DropDown");
    }
}
