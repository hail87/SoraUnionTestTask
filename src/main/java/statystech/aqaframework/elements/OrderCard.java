package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderCard extends Element {

    private static final Logger logger = LoggerFactory.getLogger(OrderCard.class);

    @Setter
    @Getter
    WebElement element;
    By locator;
    WebDriver webDriver;

    //By orderCardsBy = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[1]/div");
    By btnExpirationDate = By.xpath(".//div/div/div/div[2]/div/div/p[2]/span");
    By btnCancellationRequested = By.xpath(".//div/div/div/div[2]/div/p[3]/span");
    By btnRequestCancellation = By.xpath(".//div/div/div/div[2]/div/p[3]/a");

    public OrderCard(WebDriver webDriver, By locator) {
        this.locator = locator;
        this.webDriver = webDriver;
        setElement(webDriver.findElement(locator));
    }

    public void click() {
        waitForElementToLoad(locator, webDriver);
        if (!isVisible(locator, webDriver)) {
            logger.error("OrderCard with locator IS NOT displayed: '" + locator + "'");
        }
        if (!isEnabled(locator, webDriver)) {
            logger.error("OrderCard with locator IS NOT clickable: '" + locator + "'");
        }
        element.click();
        logger.info("OrderCard with locator clicked: " + locator);
    }

    public String getExpirationDate() {
        String buttonText = "";
        if (isExpirationDateVisible()) {
            buttonText = element.findElement(btnExpirationDate).getText();
            logger.info(("OrderCard with locator '" + locator + "' have expiration date : '" + buttonText + "'"));
        }
        return buttonText;
    }

    public boolean isExpirationDateVisible() {
        Boolean isVisible = element.findElement(btnExpirationDate).isDisplayed();
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
            isVisible = element.findElement(btnCancellationRequested).isDisplayed();
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
        new Button(webDriver, btnRequestCancellation).click();
    }

    protected boolean isVisible() {
        return super.isVisible(locator, webDriver);
    }

    public boolean isEnabled() {
        return super.isEnabled(locator, webDriver);
    }
}
