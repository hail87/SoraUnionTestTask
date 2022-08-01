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
    WebElement element;
    By locator;
    WebDriver webDriver;

    //By msgMoveToNewOrder = By.xpath("/html/body/div[2]/div[3]/div/div");
    By btnConfirm = By.xpath(".//div[2]/div/div[2]/button[2]");
    By btnCancel = By.xpath(".//div[2]/div/div[2]/button[1]");


    public Message(WebDriver webDriver, By locator) {
        this.locator = locator;
        this.webDriver = webDriver;
        waitForElementToLoad(locator, webDriver);
        setElement(webDriver.findElement(locator));
    }

    public void confirm() {
        if (!isVisible(btnConfirm, webDriver)) {
            logger.error("Button with locator IS NOT displayed: '" + locator + btnConfirm + "'");
        }
        if (!isEnabled(locator, webDriver)) {
            logger.error("Button with locator IS NOT clickable: '" + locator + btnConfirm + "'");
        }
        element.findElement(btnConfirm).click();
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
        element.findElement(btnCancel).click();
        logger.info("Button with locator clicked: " + locator + btnCancel);
        waitForElementToDisappear(btnCancel, webDriver);
    }

//    public String getText() {
//    }

    protected boolean isVisible() {
        return super.isVisible(locator, webDriver);
    }

    public boolean isEnabled() {
        return super.isEnabled(locator, webDriver);
    }
}
