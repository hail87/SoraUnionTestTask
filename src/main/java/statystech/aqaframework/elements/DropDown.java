package statystech.aqaframework.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropDown extends Element {

    private static final Logger logger = LoggerFactory.getLogger(DropDown.class);

    WebElement dropDown;

    public DropDown(WebDriver webDriver, By locator) {
        dropDown = webDriver.findElement(locator);
    }

    public void selectByVisibleText(String text) {
        WebElement webElement = dropDown.findElement(By.xpath(".//li[text()=\"" + text + "\"]"));
        webElement.click();
        logger.info("Option selected at the DropDown : " + text);
    }
}
