package statystech.aqaframework.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShipmentInformationPopUp extends Element {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentInformationPopUp.class);

    @Setter
    @Getter
    WebElement webElement;
    @Setter
    @Getter
    By locator;
    @Setter
    @Getter
    WebDriver webDriver;

    //By popUpShipmentInfo = By.xpath("/html/body/div[2]/div[3]");
    ///html/body/div[2]/div[3]/div/div[4]/button
    By checkBoxLocalDeliveryPickup = By.xpath("//div/div[3]/div[2]/label/span/input");
    By btnCompleteFulfillment = By.xpath("//button[contains(text(), 'Complete fulfillment')]");
    By txtTrackingNumber = By.xpath("//input[@name=\"trackingNumber\"]");
    By ddShippingAccount = By.xpath("/html/body/div[2]/div[3]/div/div[3]/div[3]/div");
    By txtShippingCost = By.xpath("//input[@name=\"shippingRate\"]");
    By ddCurrency = By.xpath("//input[@name=\"shippingRateCurrency\"]/..");

    public ShipmentInformationPopUp(WebDriver webDriver, By locator) {
        super(webDriver, locator);
        setLocator(locator);
        setWebDriver(webDriver);
        waitForElementToLoad(locator, webDriver);
        setWebElement(webDriver.findElement(locator));
    }

    public void fillTrackingNumber(String text) {
        new TextField(webDriver, txtTrackingNumber).fillIn(text);
    }

    public void chooseShippingAccount(int index) {
        new DropDown(webDriver, ddShippingAccount).selectByIndex(index);
    }

    public void fillShippingCost(String text) {
        new TextField(webDriver, txtShippingCost).fillIn(text);
    }

    public void chooseCurrency(String text) {
        new DropDown(webDriver, ddCurrency).selectByVisibleText(text);
    }

    public void checkboxCheck() {
        if (!isEnabled(checkBoxLocalDeliveryPickup, webDriver)) {
            logger.error("Checkbox with locator IS NOT clickable: '" + checkBoxLocalDeliveryPickup + "'");
        }
        webDriver.findElement(checkBoxLocalDeliveryPickup).click();
    }

    public void clickCompleteFulfillment() {
        new Button(webDriver, btnCompleteFulfillment).click();
    }

}
