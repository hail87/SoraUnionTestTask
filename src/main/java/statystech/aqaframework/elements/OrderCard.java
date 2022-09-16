package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.TestContext;
import statystech.aqaframework.common.Context.UiTestContext;

public class OrderCard extends Element {

    private static final Logger logger = LoggerFactory.getLogger(OrderCard.class);

    @Setter
    @Getter
    WebElement webElement;
    @Setter
    @Getter
    By locator; //needs to be reset by getXpath()
    @Setter
    @Getter
    WebDriver webDriver;

    //By orderCardsBy = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[1]/div");
    //By orderCardsInProgressBy = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[2]/div");
    By index = By.xpath(".//div/div[2]/div/div/p");
    By btnExpirationDate = By.xpath(".//div/div/div/div[2]/div/div/p[2]/span");
    By btnCancellationRequested = By.xpath(".//div/div/div/div[2]/div/p[3]/span");
    By btnRequestCancellation = By.xpath(".//div[2]/div/p[3]/a");
    By btnReset = By.xpath(".//div/div[2]/div/div[2]/span");

    public OrderCard(WebDriver webDriver, By locator) {
        super(webDriver,locator);
        setLocator(locator);
        setWebDriver(webDriver);
        waitForElementToLoad(locator, webDriver);
        setWebElement(webDriver.findElement(locator));
    }

    public OrderCard(WebElement webElement, WebDriver webDriver) {
        super(webElement, webDriver);
        setLocator(By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[1]/div"));
        setWebDriver(webDriver);
        waitForElementToLoad(locator, webDriver);
        setWebElement(webElement);
    }

    public int getIndex() {
        waitForJStoLoad(webDriver);
        scrollToElement(webElement, webDriver);
        waitForElementToLoad(locator, webDriver);
        int i = 0;
        try {
            i = Integer.parseInt(webElement.findElement(index).getText().substring(1));
        } catch (StringIndexOutOfBoundsException e) {
        }
        return i;
    }

    public void click() {
        waitForElementToLoad(locator, webDriver);
        if (!isVisible(locator, webDriver)) {
            logger.error("OrderCard with locator IS NOT displayed: '" + locator + "'");
        }
        if (!isEnabled(locator, webDriver)) {
            logger.error("OrderCard with locator IS NOT clickable: '" + locator + "'");
        }
        webElement.click();
        logger.info("OrderCard with locator clicked: " + locator);
    }

    public void clickResetBtn() {
        waitForElementToLoad(btnReset, webDriver);
        if (!isVisible(locator, webDriver)) {
            logger.error("OrderCard with locator IS NOT displayed: '" + locator + "'");
        }
        if (!isEnabled(locator, webDriver)) {
            logger.error("OrderCard with locator IS NOT clickable: '" + locator + "'");
        }
        webElement.findElement(btnReset).click();
        logger.info("Reset button with locator clicked: " + locator);
    }

    public String getExpirationDate() {
        String buttonText = "";
        if (isExpirationDateVisible()) {
            buttonText = webElement.findElement(btnExpirationDate).getText();
            logger.info(("OrderCard with locator '" + locator + "' have expiration date : '" + buttonText + "'"));
        }
        return buttonText;
    }

    public boolean isExpirationDateVisible() {
        Boolean isVisible = webElement.findElement(btnExpirationDate).isDisplayed();
        if (isVisible) {
            logger.info(("OrderCard with locator '" + locator + "' have expiration date\n"));
        } else {
            logger.info(("OrderCard with locator '" + locator + "' haven't expiration date\n"));
        }
        return isVisible;
    }

    public boolean isCancellationRequested() {
        Boolean isVisible;
        try {
            isVisible = webElement.findElement(btnCancellationRequested).isDisplayed();
        } catch (NoSuchElementException e) {
            isVisible = false;
        }
        if (isVisible) {
            logger.info(("For OrderCard with locator '" + locator + "' Cancellation Requested\n"));
        } else {
            logger.info(("For OrderCard with locator '" + locator + "' Cancellation is NOT Requested\n"));
        }
        return isVisible;
    }

    public void requestCancellation() {
        webElement.findElement(btnRequestCancellation).click();
    }

    public boolean isVisible() {
        return super.isVisible(locator, webDriver);
    }

    public boolean isDisabled() {
        return super.isEnabled(locator, webDriver);
    }
}
