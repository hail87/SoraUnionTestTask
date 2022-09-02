package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Message extends Element {

    private static final Logger logger = LoggerFactory.getLogger(Message.class);

    @Setter
    @Getter
    WebElement webElement;
    @Setter
    @Getter
    By locator;
    @Setter
    @Getter
    WebDriver webDriver;

    //By msgMoveToNewOrder = By.xpath("/html/body/div[2]/div[3]/div/div");
    By btnConfirm = By.xpath(".//div[2]/div/div[2]/button[2]");
    By btnCancel = By.xpath(".//div[2]/div/div[2]/button[1]");


    public Message(WebDriver webDriver, By locator) {
        super(webDriver,locator);
        setLocator(locator);
        setWebDriver(webDriver);
        waitForElementToLoad(locator, webDriver);
        setWebElement(webDriver.findElement(locator));
    }

    public void confirm() {
        if (!isVisible(btnConfirm, webDriver)) {
            logger.error("Button with locator IS NOT displayed: '" + locator + btnConfirm + "'");
        }
        if (!isEnabled(locator, webDriver)) {
            logger.error("Button with locator IS NOT clickable: '" + locator + btnConfirm + "'");
        }
        webElement.findElement(btnConfirm).click();
        logger.info("Button with locator clicked: " + locator + btnConfirm);
        waitForElementToDisappear(btnConfirm, webDriver);
    }

    public void cancel() {
        if (!isVisible(btnCancel, webDriver)) {
            logger.error("Button with locator IS NOT displayed: '" + locator + btnCancel + "'");
        }
        if (!isEnabled(btnCancel, webDriver)) {
            logger.error("Button with locator IS NOT clickable: '" + locator + btnCancel + "'");
        }
        webElement.findElement(btnCancel).click();
        logger.info("Button with locator clicked: " + locator + btnCancel);
        waitForElementToDisappear(btnCancel, webDriver);
    }

//    public String getText() {
//    }

    public boolean isVisible() {
        return super.isVisible(locator, webDriver);
    }

    public boolean isDisabled() {
        return super.isEnabled(locator, webDriver);
    }
}
